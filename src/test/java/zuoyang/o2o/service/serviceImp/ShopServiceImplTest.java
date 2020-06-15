package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xmlunit.builder.Input;
import zuoyang.o2o.dto.ImageHolder;
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
import java.util.List;

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
        area.setAreaId(3);
        shopCategory.setShopCategoryId(2L);

        shop.setPersonInfo(personInfo);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("test shop 13");
        shop.setShopDesc("test desc 13");
        shop.setShopAddress("test address 13");
        shop.setPhone("test phone 13");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("under review");

        File shopImage = new File ("/Users/zuoyangding/work/image/tengyuan.jpg");
        InputStream inputStream = new FileInputStream(shopImage);
        ImageHolder imageHolder = new ImageHolder(shopImage.getName(), inputStream);
        ShopExecution shopExecution = shopService.addShop(shop, imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
    }

    @Test
    void modifyShopForNull() throws ShopOperationException {
        // null shop
        ShopExecution shopExecution_1 = shopService.modifyShop(null, null);
        assertEquals(shopExecution_1.getState(), ShopStateEnum.NULL_SHOP.getState());

        //null shopId
        ShopExecution shopExecution_2 = shopService.modifyShop(new Shop(), null);
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

        ShopExecution shopExecution = shopService.modifyShop(shop, null);
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
        ImageHolder imageHolder = new ImageHolder(imgFile.getName(), imageInputStream);
        ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
        assertEquals("TEST MODIFY SHOPNAME 1", shopExecution.getShop().getShopName());

    }

    @Test
    void getShopList() {
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();

        shopCategory.setShopCategoryId(2L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.getShopList(shopCondition, 3, 3);
        assertEquals(6, shopExecution.getCount());
        for (Shop shop : shopExecution.getShopList()) {
            System.out.println(shop.getShopName());
        }
    }
}