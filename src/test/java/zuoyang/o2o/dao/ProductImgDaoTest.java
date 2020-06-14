package zuoyang.o2o.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.ProductImg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductImgDaoTest {
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    @Order(1)
    void batchInsertProductImg() {
        List<ProductImg> productImgList = new ArrayList<>();
        ProductImg img_1 = new ProductImg();
        ProductImg img_2 = new ProductImg();
        img_1.setImgAddress("test img address 1");
        img_1.setImgDesc("test img desc 1");
        img_1.setProductId(3L);
        img_1.setPriority(1);
        img_1.setCreateTime(new Date());
        img_1.setLastEditTime(new Date());

        img_2.setImgAddress("test img address 2");
        img_2.setImgDesc("test img desc 2");
        img_2.setProductId(3L);
        img_2.setPriority(2);
        img_2.setCreateTime(new Date());
        img_2.setLastEditTime(new Date());

        productImgList.add(img_1);
        productImgList.add(img_2);

        int effNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2, effNum);
    }
}