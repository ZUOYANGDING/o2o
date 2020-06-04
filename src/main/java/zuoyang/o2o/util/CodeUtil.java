package zuoyang.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        String expectedVerifyCode = (String) request.getSession().
                getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String inputVerifyCode = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        if (inputVerifyCode==null || !inputVerifyCode.equals(expectedVerifyCode)) {
            return false;
        } else {
            return true;
        }
    }
}
