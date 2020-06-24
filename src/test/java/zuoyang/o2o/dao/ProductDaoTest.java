package zuoyang.o2o.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.*;

import java.util.Date;
import java.util.List;

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

    @Test
    @Order(2)
    void updateProduct() {
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();

        productCategory.setProductCategoryId(9L);
        shop.setShopId(30L);

        product.setProductId(3L);
        product.setProductName("test product 1 update");
        product.setImgAddr("test addr update");
        product.setProductDesc("test desc update");
        product.setNormalPrice("test normal price update");
        product.setPromotePrice("test promote price update");
        product.setPriority(2);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        Product product1 = new Product();
        Shop shop1 = new Shop();

        shop1.setShopId(29L);

        product1.setProductName("test product 2 update");
        product1.setImgAddr("test addr 2 update");
        product1.setProductDesc("test desc 2 update");
        product1.setNormalPrice("test normal price 2 update");
        product1.setPromotePrice("test promote price 2 update");
        product1.setPriority(3);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setEnableStatus(1);

        product1.setShop(shop1);
        product1.setProductId(4L);

        int effNumber1 = productDao.updateProduct(product);
        assertEquals(1, effNumber1);
        int effNumber2 = productDao.updateProduct(product1);
        assertEquals(1, effNumber2);
    }

    @Test
    @Order(3)
    void queryProductByProductIdWithNullDetailImage() {
        Long productId = 3L;
        Product product = productDao.queryProductByProductId(productId);
        assertNotNull(product);
        System.out.println(product.getProductName() + " " + product.getProductDesc() + " " +
                product.getImgAddr() + " " + product.getShop().getShopId() + " " +  product.getPromotePrice() +
                product.getNormalPrice() + " " + product.getCreateTime() + " " + product.getEnableStatus() +
                product.getLastEditTime() + " " + product.getPriority() + " " +
                product.getProductCategory().getProductCategoryId());
    }

    @Test
    @Order(4)
    void queryProductByProductIdWithDetailImage() {
        Long productId = 11L;
        Product product = productDao.queryProductByProductId(productId);
        assertNotNull(product);
        System.out.println("Product Name " + product.getProductName());
        System.out.println("Product Desc " + product.getProductDesc());
        System.out.println("Product ImgAddr " + product.getImgAddr());
        System.out.println("Product shopId " + product.getShop().getShopId());
        System.out.println("Product promotePrice " + product.getPromotePrice());
        System.out.println("Product normalPrice " + product.getNormalPrice());
        System.out.println("Product LastEditTime " + product.getLastEditTime());
        System.out.println("Product CreateTime " + product.getCreateTime());
        System.out.println("Product EnableStatus " + product.getEnableStatus());
        System.out.println("Product Priority " + product.getPriority());
        System.out.println("Product CategoryId " + product.getProductCategory().getProductCategoryId());

        List<ProductImg> productImgList = product.getProductImgList();
        for (ProductImg img : productImgList) {
            System.out.println("Image id "  + img.getProductImgId());
            System.out.println("Image addr "  + img.getImgAddress());
            System.out.println("Image priority "  + img.getPriority());
            System.out.println("Image create time "  + img.getCreateTime());
            System.out.println("Image desc "  + img.getImgDesc());
        }
    }

    @Test
    @Order(5)
    void queryProductListAndCount() {
        int rowIndex = 0;
        int pageSize = 10;
        Product productCondition = new Product();
        // query by shop id;
        Shop shop = new Shop();
        shop.setShopId(40L);
        productCondition.setShop(shop);

        // query by productCategory and shop id
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(17L);
        productCondition.setProductCategory(productCategory);

        // query by productCategory, shopId and productName
        productCondition.setProductName("test product 8");

        // query by productCategory, shopId, productName and enableStatus
        productCondition.setEnableStatus(1);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        int count = productDao.queryProductCount(productCondition);
        assertEquals(1, count);
        for (Product product : productList) {
            System.out.println("Product Name " + product.getProductName());
            System.out.println("Product Desc " + product.getProductDesc());
            System.out.println("Product ImgAddr " + product.getImgAddr());
            System.out.println("Product shopId " + product.getShop().getShopId());
            System.out.println("Product promotePrice " + product.getPromotePrice());
            System.out.println("Product normalPrice " + product.getNormalPrice());
            System.out.println("Product LastEditTime " + product.getLastEditTime());
            System.out.println("Product CreateTime " + product.getCreateTime());
            System.out.println("Product EnableStatus " + product.getEnableStatus());
            System.out.println("Product Priority " + product.getPriority());
            System.out.println("Product CategoryId " + product.getProductCategory().getProductCategoryId());

            List<ProductImg> productImgList = product.getProductImgList();
            for (ProductImg img : productImgList) {
                System.out.println("Image id "  + img.getProductImgId());
                System.out.println("Image addr "  + img.getImgAddress());
                System.out.println("Image priority "  + img.getPriority());
                System.out.println("Image create time "  + img.getCreateTime());
                System.out.println("Image desc "  + img.getImgDesc());
            }
        }
    }

    @Test
    @Order(6)
    void updateProductCategoryToNull() {
        Long productCategoryId = 2L;
        int effNum = productDao.updateProductCategoryToNull(productCategoryId);
        assertEquals(1, effNum);
    }

}