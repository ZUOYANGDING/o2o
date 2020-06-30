package zuoyang.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ProductStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductCategoryService;
import zuoyang.o2o.service.ProductService;
import zuoyang.o2o.service.ShopService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    private final ShopService shopService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public ShopDetailController(ShopService shopService, ProductService productService,
                                ProductCategoryService productCategoryService) {
        this.shopService = shopService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/listshopdetailpageinfo")
    @ResponseBody
    private Map<String, Object> getShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            // get shop info
            try {
                Shop currentShop = shopService.getShopById(shopId);
                if (currentShop != null) {
                    modelMap.put("shopSuccess", true);
                    modelMap.put("shop", currentShop);
                } else {
                    modelMap.put("shopSuccess", false);
                    modelMap.put("errMsg", "cannot get shop info");
                }
            } catch (Exception e) {
                modelMap.put("shopSuccess", false);
                modelMap.put("errMsg", e.getMessage());
            }

            // get product category in this shop
            try {
                List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
                if (productCategoryList!=null && productCategoryList.size()>0) {
                    modelMap.put("productCategorySuccess", true);
                    modelMap.put("productCategoryList", productCategoryList);
                } else {
                    modelMap.put("productCategorySuccess", false);
                    modelMap.put("errMsg", "No productCategory under this shop");
                }
            } catch (Exception e) {
                modelMap.put("productCategorySuccess", false);
                modelMap.put("errMsg", e.getMessage());
            }

            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Invalid shopId");
            modelMap.put("redirect", "/o2o/frontend/index");
            return modelMap;
        }
    }

    @GetMapping("/listproductsbyshop")
    @ResponseBody
    private Map<String, Object> listProductByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // get pageIndex and pageSize
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        // get shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop currentShop = new Shop();
        if (shopId > -1) {
            currentShop.setShopId(shopId);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Invalid shopId");
            modelMap.put("redirect", "/o2o/frontend/index");
            return modelMap;
        }
        // get productCategoryId
        long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
        // get productName
        String productName = HttpServletRequestUtil.getString(request, "productName");

        if (pageIndex>-1 && pageSize>-1) {
            try {
                Product productCondition = compactProductForSearch(currentShop, productCategoryId, productName);
                ProductExecution productExecution = productService.getProductList(productCondition,
                        pageIndex, pageSize);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("productList", productExecution.getProductList());
                    modelMap.put("count", productExecution.getCount());
                } else if (productExecution.getState() == ProductStateEnum.SUCCESS_WITH_EMPTY.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Invalid pageIndex/pageSize");
        }
        return modelMap;
    }

    private Product compactProductForSearch(Shop currentShop, long productCategoryId, String productName) {
        Product productCondition = new Product();
        // set enableStatus to 1, since only search available product
        productCondition.setEnableStatus(1);
        productCondition.setShop(currentShop);

        if (productCategoryId > -1) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }

        if (productName != null) {
            productCondition.setProductName(productName);
        }

        return productCondition;
    }
}
