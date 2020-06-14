package zuoyang.o2o.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductDaoTest {
    @Autowired
    private ProductDao productDao;

    @Test
    @Order(1)
    void insertProduct() {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();

        productCategory.setProductCategoryId(1L);
        shop.setShopId(30L);

        product.setProductName("test product 1");
        product.setImgAddr("test addr");
        product.setProductDesc("test desc");
        product.setNormalPrice("test normal price");
        product.setPromotePrice("test promote price");
        product.setPriority(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        Product product1 = new Product();
        ProductCategory productCategory1 = new ProductCategory();
        Shop shop1 = new Shop();

        productCategory1.setProductCategoryId(2L);
        shop1.setShopId(29L);

        product1.setProductName("test product 2");
        product1.setImgAddr("test addr 2");
        product1.setProductDesc("test desc 2");
        product1.setNormalPrice("test normal price 2");
        product1.setPromotePrice("test promote price 2");
        product1.setPriority(2);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setEnableStatus(1);

        product1.setProductCategory(productCategory1);
        product1.setShop(shop1);

        int effNum1 = productDao.insertProduct(product);
        assertEquals(1, effNum1);

        int effNum2 = productDao.insertProduct(product1);
        assertEquals(1, effNum2);

    }
}