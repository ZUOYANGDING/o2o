package zuoyang.o2o.web.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zuoyang.o2o.util.wechat.SignUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {
    @GetMapping
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) {
        log.info("check weChat signature");
        // where signature contains token, and timestamp and nonce in request
        String signature = request.getParameter("signature");
        String timeStamp = request.getParameter("timestamp");
        // random number
        String nonce = request.getParameter("nonce");
        // random string
        String echoStr = request.getParameter("echostr");

        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (SignUtil.checkUserSignature(signature, timeStamp, nonce)) {
                log.debug("check signature success.......");
                out.println(echoStr);
            } else {
                out.println("signature does not match");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
