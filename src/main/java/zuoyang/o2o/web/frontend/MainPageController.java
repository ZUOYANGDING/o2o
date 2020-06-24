package zuoyang.o2o.web.frontend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.entity.HeadLine;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.exception.HeadLineOperationException;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.HeadLineService;
import zuoyang.o2o.service.ShopCategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/frontend")
public class MainPageController {
    private final ShopCategoryService shopCategoryService;
    private final HeadLineService headLineService;

    public MainPageController(ShopCategoryService shopCategoryService, HeadLineService headLineService) {
        this.shopCategoryService = shopCategoryService;
        this.headLineService = headLineService;
    }

    @GetMapping("/listmainpageinfo")
    @ResponseBody
    private Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        // get root shop category list
        try {
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
            if (shopCategoryList==null || shopCategoryList.size()==0) {
                modelMap.put("shopCategorySuccess", true);
            } else {
                modelMap.put("shopCategorySuccess", true);
                modelMap.put("shopCategoryList", shopCategoryList);
            }
        } catch (ShopOperationException e) {
            modelMap.put("shopCategorySuccess", false);
            modelMap.put("shopCategoryErrMsg", e.getMessage());
        }

        // get headline list with enable status 1
        try {
            HeadLine headLine = new HeadLine();
            headLine.setEnableStatus(1);
            List<HeadLine> headLineList = headLineService.getHeadLineList(headLine);
            if (headLineList==null || headLineList.size()==0) {
                modelMap.put("headLineSuccess", true);
            } else {
                modelMap.put("headLineSuccess", true);
                modelMap.put("headLineList", headLineList);
            }
        } catch (HeadLineOperationException e) {
            modelMap.put("headLineSuccess", false);
            modelMap.put("headLineErrMsg", e.getMessage());
        }

        return modelMap;
    }
}
