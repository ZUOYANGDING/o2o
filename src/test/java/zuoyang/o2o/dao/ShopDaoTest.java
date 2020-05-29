package zuoyang.o2o.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    void insertShop() {
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

        shop.setShopName("foo shop");
        shop.setShopDesc("foo desc");
        shop.setShopAddress("foo address");
        shop.setPhone("foo phone");
        shop.setShopImg("foo img");
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("under review");
        shop.setPriority(1);
        int returnNumber = shopDao.insertShop(shop);
        assertEquals(1, returnNumber);
    }

    @Test
    void updateShop() {
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        Area area = new Area();

        shop.setShopId(1L);
        area.setAreaId(2);
        shop.setPersonInfo(personInfo);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopDesc("foo desc test update");
        shop.setShopAddress("foo address test update");
        int returnNumber = shopDao.updateShop(shop);
        assertEquals(1, returnNumber);
    }
}