package zuoyang.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.AreaService;
import zuoyang.o2o.service.ShopCategoryService;
import zuoyang.o2o.service.ShopService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
    private final ShopCategoryService shopCategoryService;
    private final AreaService areaService;
    private final ShopService shopService;

    public ShopListController(ShopCategoryService shopCategoryService, AreaService areaService,
                              ShopService shopService) {
        this.shopCategoryService = shopCategoryService;
        this.areaService = areaService;
        this.shopService = shopService;
    }

    //get root or first level shop category list and list of all areas
    @GetMapping("/listshopspageinfo")
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // get parent shop Id from frontend
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1) {
            // parentId exist means redirect from the first level of shop category, fetch all shop categories
            // under this parentId
            try {
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                ShopCategory shopCategoryCondition = new ShopCategory();
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            // parentId does not exist means redirect from the "All shop category", fetch all root shop categories
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);

        // get list of area
        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            if (areaList!=null && areaList.size()!=0) {
                modelMap.put("success", true);
                modelMap.put("areaList", areaList);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    //return shop list under search restriction
    @GetMapping("/listshops")
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");

        if (pageIndex>-1 && pageSize>-1) {
            // get root category id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            // get category id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            // get shop name
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            // get area id
            int areaId = HttpServletRequestUtil.getInteger(request, "areaId");
            // compact all search restrict
            Shop shopCondition = compactSearchRestriction(parentId, shopCategoryId, shopName, areaId);

            try {
                ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);
                if (shopExecution.getShopList()!=null && shopExecution.getShopList().size()>0) {
                    modelMap.put("success", true);
                    modelMap.put("shopList", shopExecution.getShopList());
                    modelMap.put("count", shopExecution.getCount());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "No shop under search, Please try again");
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "pageIndex or pageSize must be non-negative");
        }
        return modelMap;
    }

    private Shop compactSearchRestriction(long parentId, long shopCategoryId, String shopName, int areaId) {
        Shop shopCondition = new Shop();
        if (parentId > -1) {
            ShopCategory parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setParent(parent);
            shopCondition.setShopCategory(shopCategory);
        }
        if (shopCategoryId > -1) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        if (areaId > -1) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        return shopCondition;
    }
}
