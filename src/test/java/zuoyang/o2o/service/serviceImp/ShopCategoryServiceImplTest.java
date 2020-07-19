package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.service.CacheService;
import zuoyang.o2o.service.ShopCategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopCategoryServiceImplTest {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private CacheService cacheService;

    @Test
    @Order(1)
    void getRootShopCategoryList() {
//        cacheService.removeCache(shopCategoryService.SHOP_CATEGORY_LIST_KEY);
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
        assertEquals(5, shopCategoryList.size());
        shopCategoryList.stream().forEach(shopCategory -> {
            System.out.println(shopCategory.getShopCategoryName());
        });
    }

    @Test
    @Order(2)
    void getShopCategoryListUnderRootLevel() {
//        cacheService.removeCache(shopCategoryService.SHOP_CATEGORY_LIST_KEY);
        ShopCategory shopCategory = new ShopCategory();
        ShopCategory parentShopCategory = new ShopCategory();
        parentShopCategory.setShopCategoryId(4L);
        shopCategory.setParent(parentShopCategory);
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);
        assertEquals(1, shopCategoryList.size());
        shopCategoryList.stream().forEach(item -> {
            System.out.println(item.getShopCategoryName());
        });
    }

    @Test
    @Order(3)
    void getShopCategoryListOfSubCategories() {
//        cacheService.removeCache(shopCategoryService.SHOP_CATEGORY_LIST_KEY);
        ShopCategory shopCategory = new ShopCategory();
        List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);
        assertEquals(5, shopCategoryList.size());
        shopCategoryList.stream().forEach(item -> {
            System.out.println(item.getShopCategoryName());
        });
    }
}