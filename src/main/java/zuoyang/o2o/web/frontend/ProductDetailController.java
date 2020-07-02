package zuoyang.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    private final ProductService productService;

    public ProductDetailController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/listproductdetailpageinfo")
    @ResponseBody
    private Map<String, Object> getProductDetail(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        if (productId > -1) {
            try {
                Product product = productService.getProductInfo(productId);
                if (product != null) {
                    modelMap.put("success", true);
                    modelMap.put("product", product);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "cannot find matching product");
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("redirect", "/o2o/frontend/shoplist");
        }
        return modelMap;
    }
}
