package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.service.ProductCategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductCategoryServiceImplTest {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    void getProductCategoryList() {
        Long shopId = 29L;
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
        assertEquals(2, productCategoryList.size());
        assertEquals("product category test 2", productCategoryList.get(0).getProductCategoryName());
    }
}