package zuoyang.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zuoyang.o2o.dto.ProductCategoryExecution;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ProductCategoryStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductCategoryService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
     */
    @GetMapping("/getproductcategorylist")
    @ResponseBody
    private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
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

    /**
     *
     * @param productCategoryList
     * @param request
     * @return
     */
    @PostMapping("/addproductcategories")
    @ResponseBody
    private Map<String, Object> addProductCategoryList(@RequestBody List<ProductCategory> productCategoryList,
                                                       HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Object currentShop = request.getSession().getAttribute("currentShop");
        if (currentShop == null) {
            modelMap.put("success", false);
            modelMap.put("redirect", true);
            modelMap.put("url", "/o2o/shopadmin/shoplist");
        } else {
            Shop shop = (Shop) currentShop;
            if (productCategoryList != null && productCategoryList.size()!=0) {
                for (ProductCategory productCategory : productCategoryList) {
                    productCategory.setShopId(shop.getShopId());
                    productCategory.setCreateTime(new Date());
                    productCategory.setLastEditTime(new Date());
                }
                try {
                    ProductCategoryExecution productCategoryExecution =
                            productCategoryService.batchInsertProductCategory(productCategoryList);
                    if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);
                        modelMap.put("redirect", false);
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("redirect", false);
                        modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                    }
                } catch (ProductOperationException e) {
                    modelMap.put("success", false);
                    modelMap.put("redirect", false);
                    modelMap.put("errMsg", e.getMessage());
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("redirect", false);
                modelMap.put("errMsg", "Add At least One Product Category");
            }
        }
        return modelMap;
    }
}
