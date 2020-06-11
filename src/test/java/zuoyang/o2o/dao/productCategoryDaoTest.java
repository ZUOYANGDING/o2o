package zuoyang.o2o.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.ProductCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class productCategoryDaoTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    void queryProductCategory() {
        Long shopId = 29L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        assertEquals(2, productCategoryList.size());
    }

    @Test
    void batchInsertProductCategory() {
        ProductCategory pc1 = new ProductCategory();
        ProductCategory pc2 = new ProductCategory();
        ProductCategory pc3 = new ProductCategory();
        ProductCategory pc4 = new ProductCategory();
        pc1.setShopId(31L);
        pc1.setProductCategoryName("product category name 1");
        pc1.setPriority(3);
        pc1.setCreateTime(new Date());
        pc1.setLastEditTime(new Date());

        pc2.setShopId(31L);
        pc2.setProductCategoryName("product category name 2");
        pc2.setPriority(2);
        pc2.setCreateTime(new Date());
        pc2.setLastEditTime(new Date());

        pc3.setShopId(40L);
        pc3.setProductCategoryName("product category name 3");
        pc3.setPriority(1);
        pc3.setCreateTime(new Date());
        pc3.setLastEditTime(new Date());

        pc4.setShopId(40L);
        pc4.setProductCategoryName("product category name 4");
        pc4.setPriority(4);
        pc4.setCreateTime(new Date());
        pc4.setLastEditTime(new Date());

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(pc1);
        productCategoryList.add(pc2);
        productCategoryList.add(pc3);
        productCategoryList.add(pc4);
        int effNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(4, effNum);
    }
}