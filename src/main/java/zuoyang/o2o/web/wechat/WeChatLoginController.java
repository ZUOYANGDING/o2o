package zuoyang.o2o.web.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zuoyang.o2o.dto.WeChatAuthExecution;
import zuoyang.o2o.dto.WeChatUser;
import zuoyang.o2o.dto.WeChatUserAccessToken;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.WeChatAuth;
import zuoyang.o2o.enums.WeChatAuthStateEnum;
import zuoyang.o2o.exception.WeChatAuthOperationException;
import zuoyang.o2o.service.PersonInfoService;
import zuoyang.o2o.service.WeChatAuthService;
import zuoyang.o2o.util.wechat.WeChatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wechatlogin")
@Slf4j
public class WeChatLoginController {
    private final WeChatUtil weChatUtil;
    private final WeChatAuthService weChatAuthService;
    private final PersonInfoService personInfoService;
    private final String FRONT_END = "1";
    private final String SHOP_END = "2";


    public WeChatLoginController(WeChatUtil weChatUtil, WeChatAuthService weChatAuthService,
                                 PersonInfoService personInfoService) {
        this.weChatUtil = weChatUtil;
        this.weChatAuthService = weChatAuthService;
        this.personInfoService = personInfoService;
    }

    @GetMapping("/logincheck")
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("start WeChat login check........");

        String code = request.getParameter("code");
        String roleType = request.getParameter("state");
        log.debug("code: " + code);
        log.debug("state: " + roleType);
        WeChatUser weChatUser = null;
        String openId = null;
        WeChatAuth weChatAuth = null;
        String accessToken = null;
        if (code != null) {
            WeChatUserAccessToken weChatUserAccessToken;
            try {
                weChatUserAccessToken = weChatUtil.getUserAccessToken(code);
                if (weChatUserAccessToken != null) {
                    accessToken = weChatUserAccessToken.getAccessToken();
                    log.debug("accessToken Str: " + accessToken);
                    openId = weChatUserAccessToken.getOpenId();
                    log.debug("opend Id: " + openId);
                    weChatUser = weChatUtil.getUserInfo(accessToken, openId);
                }
                log.debug("Wechat User: " + weChatUser);
                if (weChatUser != null && openId!=null) {
                    request.getSession().setAttribute("openId", openId);
                    weChatAuth = weChatAuthService.getWeChatAuthByOpenId(weChatUser.getOpenId());
                } else {
                    log.error("cannot get weChatUser...");
                }
            } catch (Exception e) {
                log.error("get wechat user login check failed....");
                e.printStackTrace();
            }
        }
        // create new weChatAuth for new weChat user, even the user have account login without weChat
        // there is no hook from weChat login to a existing user
        if (weChatAuth == null && weChatUser!=null) {
            weChatAuth = new WeChatAuth();
            weChatAuth.setOpenId(weChatUser.getOpenId());
            PersonInfo personInfo = weChatUtil.createPersonInfoFromWeChatUser(weChatUser);
            personInfo.setUserType(1);
            weChatAuth.setPersonInfo(personInfo);
            try {
                WeChatAuthExecution weChatAuthExecution = weChatAuthService.addWeChatAuth(weChatAuth);
                if (weChatAuthExecution.getState() == WeChatAuthStateEnum.SUCCESS.getState()) {
                    log.debug("userId: " + weChatAuthExecution.getWeChatAuth().getPersonInfo().getUserId());
                    personInfo = personInfoService.getPersonInfoById(weChatAuthExecution.getWeChatAuth().
                            getPersonInfo().getUserId());
                    if (personInfo != null) {
                        request.getSession().setAttribute("user", personInfo);
                        if (roleType.equalsIgnoreCase(FRONT_END)) {
                            return "frontend/index";
                        } else {
                            return "shop/shopList";
                        }
                    } else {
                        log.error("No matching user when get personInfo....");
                        return null;
                    }
                } else {
                    log.error(weChatAuthExecution.getStateInfo());
                    return null;
                }
            } catch (WeChatAuthOperationException e) {
                log.error(e.getMessage());
                return null;
            }
        } else if (weChatAuth!=null && weChatUser!=null){
            log.debug("userId: " + weChatAuth.getPersonInfo().getUserId());
            request.getSession().setAttribute("user", weChatAuth.getPersonInfo());
            if (roleType.equalsIgnoreCase(FRONT_END)) {
                return "frontend/index";
            } else {
                return "shop/shopList";
            }
        } else {
            return null;
        }
    }
}
