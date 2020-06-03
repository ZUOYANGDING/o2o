package zuoyang.o2o.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
    @GetMapping("/index.html")
    public String indexController() {
        return "index";
    }
}
