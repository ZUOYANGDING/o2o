package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ProductStateEnum;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ProductService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceImplTest {
    @Autowired
    ProductService productService;

    @Order(1)
    @Test
    void addProduct() throws ShopOperationException, FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();
        shop.setShopId(31L);
        productCategory.setProductCategoryId(6L);

        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("test product 6");
        product.setPriority(1);

        // Thumbnail
        File thumbnailFile = new File("/Users/zuoyangding/work/image/ff1.jpeg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(), is);

        // detail image
        File detailFile_1 = new File("/Users/zuoyangding/work/image/ff2.jpeg");
        File detailFile_2 = new File("/Users/zuoyangding/work/image/ff3.jpeg");
        InputStream is_1 = new FileInputStream(detailFile_1);
        InputStream is_2 = new FileInputStream(detailFile_2);
        ImageHolder imageHolder1 = new ImageHolder(detailFile_1.getName(), is_1);
        ImageHolder imageHolder2 = new ImageHolder(detailFile_2.getName(), is_2);
        List<ImageHolder> imageHolderList = new ArrayList<>();
        imageHolderList.add(imageHolder1);
        imageHolderList.add(imageHolder2);

        ProductExecution productExecution = productService.addProduct(product, imageHolder, imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }

    @Order(2)
    @Test
    void addProductErrorNullRequestObject() {
        // Test for null product Shop, ShopId;
//        ProductExecution productExecution = productService.addProduct(null, null, null);
//        assertEquals(ProductStateEnum.EMPTY_ELEMENT.getState(), productExecution.getState());
//
//        Product product = new Product();
//        ProductExecution productExecution1 = productService.addProduct(product, null, null);
//        assertEquals(ProductStateEnum.EMPTY_ELEMENT.getState(), productExecution1.getState());
//
//        Product product1 = new Product();
//        Shop shop = new Shop();
//        product1.setShop(shop);
//        ProductExecution productExecution2 = productService.addProduct(product, null, null);
//        assertEquals(ProductStateEnum.EMPTY_ELEMENT.getState(), productExecution2.getState());

        // Test for null thumbnail, detail image
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();
        shop.setShopId(31L);
        productCategory.setProductCategoryId(6L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("test product 4");
        product.setPriority(2);
        ProductExecution productExecution = productService.addProduct(product, null, null);
        assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }

//    @Order(3)
//    @Test
//    void addProductErrorInThumbnailCreate() throws ShopOperationException, FileNotFoundException{
//        Product product = new Product();
//        Shop shop = new Shop();
//        ProductCategory productCategory = new ProductCategory();
//        shop.setShopId(31L);
//        productCategory.setProductCategoryId(6L);
//        product.setShop(shop);
//        product.setProductCategory(productCategory);
//        product.setProductName("test product 5");
//        product.setPriority(2);
//
//        File thumbnailFile = new File("/Users/zuoyangding/work/image/ff1.jpeg");
//        InputStream is = new FileInputStream(thumbnailFile);
//        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(), is);
//        ProductExecution productExecution = productService.addProduct(product, imageHolder, null);
//        assertEquals(ProductStateEnum.INNER_ERROR.getState(), productExecution.getState());
//    }
//
//    @Order(4)
//    @Test
//    void addProductErrorInDetailImageCreate() throws ShopOperationException, FileNotFoundException{
//        Product product = new Product();
//        Shop shop = new Shop();
//        ProductCategory productCategory = new ProductCategory();
//        shop.setShopId(32L);
//        productCategory.setProductCategoryId(6L);
//        product.setShop(shop);
//        product.setProductCategory(productCategory);
//        product.setProductName("test product 6");
//        product.setPriority(2);
//
//        File thumbnailFile = new File("/Users/zuoyangding/work/image/ff1.jpeg");
//        InputStream is = new FileInputStream(thumbnailFile);
//        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(), is);
//
//        File detailFile_1 = new File("/Users/zuoyangding/work/image/ff2.jpeg");
//        File detailFile_2 = new File("/Users/zuoyangding/work/image/ff3.jpeg");
//        InputStream is_1 = new FileInputStream(detailFile_1);
//        InputStream is_2 = new FileInputStream(detailFile_2);
//        ImageHolder imageHolder1 = new ImageHolder(detailFile_1.getName(), is_1);
//        ImageHolder imageHolder2 = new ImageHolder(detailFile_2.getName(), is_2);
//        List<ImageHolder> imageHolderList = new ArrayList<>();
//        imageHolderList.add(imageHolder1);
//        imageHolderList.add(imageHolder2);
//
//        ProductExecution productExecution = productService.addProduct(product, imageHolder, imageHolderList);
//        assertEquals(ProductStateEnum.INNER_ERROR.getState(), productExecution.getState());
//    }



}