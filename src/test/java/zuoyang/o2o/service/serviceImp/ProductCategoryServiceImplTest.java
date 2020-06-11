package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.dto.ProductCategoryExecution;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.service.ProductCategoryService;

import java.util.ArrayList;
import java.util.Date;
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

    @Test
    void batchInsertProductCategory() {
        ProductCategory pc1 = new ProductCategory();
        ProductCategory pc2 = new ProductCategory();
        ProductCategory pc3 = new ProductCategory();
        ProductCategory pc4 = new ProductCategory();
        pc1.setShopId(31L);
        pc1.setProductCategoryName("product category name 5");
        pc1.setPriority(3);
        pc1.setCreateTime(new Date());
        pc1.setLastEditTime(new Date());

        pc2.setShopId(31L);
        pc2.setProductCategoryName("product category name 6");
        pc2.setPriority(2);
        pc2.setCreateTime(new Date());
        pc2.setLastEditTime(new Date());

        pc3.setShopId(40L);
        pc3.setProductCategoryName("product category name 7");
        pc3.setPriority(1);
        pc3.setCreateTime(new Date());
        pc3.setLastEditTime(new Date());

        pc4.setShopId(40L);
        pc4.setProductCategoryName("product category name 8");
        pc4.setPriority(4);
        pc4.setCreateTime(new Date());
        pc4.setLastEditTime(new Date());

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(pc1);
        productCategoryList.add(pc2);
        productCategoryList.add(pc3);
        productCategoryList.add(pc4);
        ProductCategoryExecution productCategoryExecution =
                productCategoryService.batchInsertProductCategory(productCategoryList);
        assertEquals(1, productCategoryExecution.getState());
        assertEquals("Operation Succeed", productCategoryExecution.getStateInfo());
    }

    @Test
    void batchInsertProductCategoryWithEmptyList() {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        ProductCategoryExecution productCategoryExecution =
                productCategoryService.batchInsertProductCategory(productCategoryList);
        assertEquals(-1002, productCategoryExecution.getState());
        assertEquals("INVALID NUMBER OF ADD", productCategoryExecution.getStateInfo());
    }
}