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

    @Test
    @Order(3)
    void modifyProductWithoutThumbnailAndImage()  throws FileNotFoundException{
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();
        shop.setShopId(31L);
        productCategory.setProductCategoryId(6L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductId(5L);
        product.setProductName("test product 3 update for empty thumbnail");
        ImageHolder thumbnail = new ImageHolder();
        List<ImageHolder> imageHolderList = new ArrayList<>();
//        // Thumbnail
//        File thumbnailFile = new File("/Users/zuoyangding/work/image/ff1.jpeg");
//        InputStream is = new FileInputStream(thumbnailFile);
//        thumbnail.setImageName(thumbnailFile.getName());
//        thumbnail.setImage(is);
//
//        // detail image
//        File detailFile_1 = new File("/Users/zuoyangding/work/image/ff2.jpeg");
//        File detailFile_2 = new File("/Users/zuoyangding/work/image/ff3.jpeg");
//        InputStream is_1 = new FileInputStream(detailFile_1);
//        InputStream is_2 = new FileInputStream(detailFile_2);
//        ImageHolder imageHolder1 = new ImageHolder(detailFile_1.getName(), is_1);
//        ImageHolder imageHolder2 = new ImageHolder(detailFile_2.getName(), is_2);
//        imageHolderList.add(imageHolder1);
//        imageHolderList.add(imageHolder2);
//
        ProductExecution productExecution = productService.modifyProduct(product, thumbnail, imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }

    @Test
    @Order(4)
    void modifyProduct() throws FileNotFoundException{
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();
        shop.setShopId(40L);
        productCategory.setProductCategoryId(8L);
        product.setProductId(11L);
        product.setProductName("test product 7 update");
        product.setShop(shop);
        product.setProductCategory(productCategory);

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
        ProductExecution productExecution = productService.modifyProduct(product, imageHolder, imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), productExecution.getState());
    }

    @Test
    @Order(5)
    void getProductList() {
        int pageIndex = 0;
        int pageSize = 3;
        Product productCondition = new Product();
        productCondition.setProductName("product 8");
        ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());

        // with no result for search
        int pageIndex_1 = 0;
        int pageSize_1 = 3;
        Product productCondition_1 = new Product();
        productCondition_1.setProductName("product 99");
        ProductExecution pe_1 = productService.getProductList(productCondition_1, pageIndex_1, pageSize_1);
        assertEquals(ProductStateEnum.SUCCESS_WITH_EMPTY.getState(), pe_1.getState());

        int pageIndex_2 = 0;
        int pageSize_2 = 3;
        Product productCondition_2 = new Product();
        Shop shop = new Shop();
        shop.setShopId(40L);
        productCondition_2.setShop(shop);
        ProductExecution pe_2 = productService.getProductList(productCondition_2, pageIndex_2, pageSize_2);
        assertEquals(3, pe_2.getProductList().size());
        pe_2.getProductList().stream().forEach((Product product) ->{
            System.out.println(product.getProductName());
        });
    }

}