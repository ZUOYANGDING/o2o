package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xmlunit.builder.Input;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.enums.ShopStateEnum;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ShopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShopServiceImplTest {
    @Autowired
    private ShopService shopService;

    @Test
    void addShop() throws ShopOperationException, FileNotFoundException {
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

        shop.setShopName("foo shop 6");
        shop.setShopDesc("foo desc 6");
        shop.setShopAddress("foo address 6");
        shop.setPhone("foo phone 6");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("under review");

        File shopImage = new File ("/Users/zuoyangding/work/image/fgo.jpg");
        InputStream inputStream = new FileInputStream(shopImage);
        ShopExecution shopExecution = shopService.addShop(shop, inputStream, shopImage.getName());
        assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
    }

    @Test
    void modifyShopForNull() throws ShopOperationException {
        // null shop
        ShopExecution shopExecution_1 = shopService.modifyShop(null, null, null);
        assertEquals(shopExecution_1.getState(), ShopStateEnum.NULL_SHOP.getState());

        //null shopId
        ShopExecution shopExecution_2 = shopService.modifyShop(new Shop(), null, null);
        assertEquals(shopExecution_2.getState(), ShopStateEnum.NULL_SHOPID.getState());
    }

    @Test
    void modifyShopWithNullImage() throws ShopOperationException {
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        Area area = new Area();

        personInfo.setUserId(1L);
        area.setAreaId(1);
        shopCategory.setShopCategoryId(1L);
        shop.setShopId(29L);

        shop.setPersonInfo(personInfo);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("TEST MODIFY SHOPNAME");
        shop.setShopDesc("TEST MODIFY SHOPDESC");
        shop.setShopAddress("TEST MODIFY SHOPADDR");
        shop.setPhone("TEST MODIFY SHOPPHONE");
        shop.setEnableStatus(ShopStateEnum.PASS.getState());
        shop.setAdvice("PASS REVIEW");

        ShopExecution shopExecution = shopService.modifyShop(shop, null, null);
        assertEquals("TEST MODIFY SHOPNAME", shopExecution.getShop().getShopName());
    }

    @Test
    void modifyShop() throws ShopOperationException, FileNotFoundException{
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        Area area = new Area();

        personInfo.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(2L);
        shop.setShopId(31L);

        shop.setPersonInfo(personInfo);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("TEST MODIFY SHOPNAME 1");
        shop.setShopDesc("TEST MODIFY SHOPDESC 1");
        shop.setShopAddress("TEST MODIFY SHOPADDR 1");
        shop.setPhone("TEST MODIFY SHOPPHONE 1");
        shop.setEnableStatus(ShopStateEnum.PASS.getState());
        shop.setAdvice("PASS REVIEW");

        File imgFile = new File("/Users/zuoyangding/work/image/tengyuan.jpg");
        InputStream imageInputStream = new FileInputStream(imgFile);
        ShopExecution shopExecution = shopService.modifyShop(shop, imageInputStream, imgFile.getName());
        assertEquals("TEST MODIFY SHOPNAME 1", shopExecution.getShop().getShopName());

    }
}