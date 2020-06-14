package zuoyang.o2o.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.enums.ShopStateEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    void insertShop() throws FileNotFoundException {
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

        shop.setShopName("test shop 2");
        shop.setShopDesc("test desc 2");
        shop.setShopAddress("test address");
        shop.setPhone("test phone");
        shop.setShopImg("test img");
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

    @Test
    void queryByShopId() {
        Shop nullShop = shopDao.queryByShopId(1L);
        assertNull(nullShop);
        Shop shop = shopDao.queryByShopId(31L);
        assertNotNull(shop);
        assertEquals("test name 2", shop.getShopName());
        assertEquals("test desc 2", shop.getShopDesc());
        assertEquals("123456", shop.getPhone());
        assertEquals("Fremont", shop.getArea().getAreaName());
        assertEquals("mac shop", shop.getShopCategory().getShopCategoryName());
    }

    @Test
    void queryShopListAndShopCountByOwnerId() {
        Shop shopCondition = new Shop();
        PersonInfo personInfo = new PersonInfo();

        personInfo.setUserId(1L);
        shopCondition.setPersonInfo(personInfo);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(15, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }

    @Test
    void queryShopListAndShopCountByCategoryId() {
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();

        shopCategory.setShopCategoryId(2L);
        shopCondition.setShopCategory(shopCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 3);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(6, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }

    @Test
    void queryShopListAndShopCountByAreaId() {
        Shop shopCondition = new Shop();
        Area area = new Area();

        area.setAreaId(3);
        shopCondition.setArea(area);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 2);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(4, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }

    @Test
    void queryShopListAndShopCountByShopName() {
        Shop shopCondition = new Shop();

        shopCondition.setShopName("test shop");
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(12, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }

    @Test
    void queryShopListAndShopCountByShopStatus() {
        Shop shopCondition = new Shop();

        shopCondition.setEnableStatus(ShopStateEnum.CHECK.getState());
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(13, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }

    @Test
    void queryShopListAndShopCountForSameParentShopCategory() {
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        Shop parentShop = new Shop();
        ShopCategory parentShopCategory = new ShopCategory();

        parentShopCategory.setShopCategoryId(1L);
        parentShop.setShopCategory(parentShopCategory);
        shopCategory.setShopCategoryId(2L);
        shopCondition.setShopCategory(shopCategory);

        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int count = shopDao.queryShopCount(shopCondition);
        assertEquals(6, count);
        for (Shop shop : shopList) {
            System.out.println(shop.getShopName());
        }
    }
}