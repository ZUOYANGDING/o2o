package zuoyang.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping("/shopOperation")
    public String shopOperation() {
        return "shop/shopOperation";
    }

    @RequestMapping("/shoplist")
    public String shopList() {
        return "shop/shopList";
    }

    @RequestMapping("/shopmanagement")
    public String shopManagement() {
        return "shop/shopManagement";
    }

    @RequestMapping("/productcategorymanagement")
    public String productCategoryManagement() {
        return "shop/productCategoryManagement";
    }

    @RequestMapping("/productoperation")
    public String productOperation() {
        return "shop/productOperation";
    }

    @RequestMapping("/productmanagement")
    public String productManagement() {
        return "shop/productManagement";
    }
}
