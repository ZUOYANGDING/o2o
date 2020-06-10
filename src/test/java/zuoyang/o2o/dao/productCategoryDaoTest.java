package zuoyang.o2o.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.ProductCategory;

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
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(shopId);
        assertEquals(2, productCategoryList.size());
    }
}