package zuoyang.o2o.web.local;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/local")
public class LocalController {
    @RequestMapping("/accountbind")
    private String accountBind() {
        return "local/accountbind";
    }
}
