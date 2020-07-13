package zuoyang.o2o.web.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zuoyang.o2o.dto.WeChatUser;
import zuoyang.o2o.dto.WeChatUserAccessToken;
import zuoyang.o2o.util.wechat.WeChatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wechatlogin")
@Slf4j
public class WeChatLoginController {
    private final WeChatUtil weChatUtil;

    public WeChatLoginController(WeChatUtil weChatUtil) {
        this.weChatUtil = weChatUtil;
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
        String auth = null;
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
                }

            } catch (Exception e) {
                log.error("get wechat user login check failed....");
                e.printStackTrace();
            }
        }
        //TODO logic to transfer the wechatUser into wechatAuth and personalInfo
        if (weChatUser != null) {
            return "frontend/index";
        } else {
            return null;
        }
    }
}
