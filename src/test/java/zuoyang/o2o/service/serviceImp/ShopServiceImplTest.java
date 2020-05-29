package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.enums.ShopStateEnum;
import zuoyang.o2o.service.ShopService;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShopServiceImplTest {
    @Autowired
    private ShopService shopService;

    @Test
    void addShop() {
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        Area area = new Area();

        personInfo.setUserId(1L);
        area.setAreaId(1);
        shopCategory.setShopCategoryId(1L);

        shop.setPersonInfo(personInfo);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("foo shop 2");
        shop.setShopDesc("foo desc 2");
        shop.setShopAddress("foo address 2");
        shop.setPhone("foo phone 2");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("under review");

        File shopImage = new File ("/Users/zuoyangding/work/image/fgo.jpg");
        ShopExecution shopExecution = shopService.addShop(shop, shopImage);
        assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
    }
}