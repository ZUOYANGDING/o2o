package zuoyang.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.dto.LocalAuthExecution;
import zuoyang.o2o.entity.LocalAuth;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.enums.LocalAuthStateEnum;
import zuoyang.o2o.exception.LocalAuthOperationException;
import zuoyang.o2o.service.LocalAuthService;
import zuoyang.o2o.util.CodeUtil;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/local")
public class LocalAuthController {
    private final LocalAuthService localAuthService;

    public LocalAuthController(LocalAuthService localAuthService) {
        this.localAuthService = localAuthService;
    }

    @PostMapping("/bindlocalauth")
    @ResponseBody
    private Map<String, Object> bindLocalAuth (HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Invalid verification code");
            return modelMap;
        }
        // get necessary message from frontend
        String password = HttpServletRequestUtil.getString(request, "password");
        String username = HttpServletRequestUtil.getString(request, "username");
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (password!=null && username!=null & personInfo!=null && personInfo.getUserId()!=null) {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setPersonInfo(personInfo);
            localAuth.setPassword(password);
            localAuth.setUsername(username);
            try {
                LocalAuthExecution localAuthExecution = localAuthService.createNewLocalAuth(localAuth);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else if (localAuthExecution.getState() == LocalAuthStateEnum.BIND_ALREADY.getState()){
                    modelMap.put("success", false);
                    modelMap.put("errMsg", LocalAuthStateEnum.BIND_ALREADY.getStateInfo());
                } else if (localAuthExecution.getState() == LocalAuthStateEnum.MISSING_ARGS.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", LocalAuthStateEnum.MISSING_ARGS.getStateInfo());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", LocalAuthStateEnum.INNER_ERROR.getStateInfo());
                }
            } catch (LocalAuthOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Miss require arguments");
        }
        return modelMap;
    }

    
}
