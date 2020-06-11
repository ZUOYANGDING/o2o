package zuoyang.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ProductCategoryStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductCategoryService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryManagementController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     *
     * @param request contains shop session, in getShopManagementInfo of shopManagementController
     * @return
     * @throws ProductOperationException
     */
    @GetMapping("/getproductcategorylist")
    @ResponseBody
    private Map<String, Object> getProductCategoryList(HttpServletRequest request) throws ProductOperationException {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop!=null && currentShop.getShopId()>0) {
            List<ProductCategory> productCategoryList =
                    productCategoryService.getProductCategoryList(currentShop.getShopId());
            if (productCategoryList != null && productCategoryList.size()>0) {
                modelMap.put("success", true);
                modelMap.put("productCategoryList", productCategoryList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "No product in this Shop");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
        }
        return modelMap;
    }
}
