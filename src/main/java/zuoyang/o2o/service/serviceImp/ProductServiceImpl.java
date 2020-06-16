package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.ProductDao;
import zuoyang.o2o.dao.ProductImgDao;
import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductImg;
import zuoyang.o2o.enums.ProductStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductService;
import zuoyang.o2o.util.ImageUtil;
import zuoyang.o2o.util.PathUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductImgDao productImgDao;

    public ProductServiceImpl(ProductDao productDao, ProductImgDao productImgDao) {
        this.productDao = productDao;
        this.productImgDao = productImgDao;
    }

    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder productThumbnail,
                                       List<ImageHolder> productImgList) throws ProductOperationException {
        if (product!=null && product.getShop()!=null && product.getShop().getShopId()!=null) {
            //set up basic info for product
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);

            //set up product thumbnail
            try {
                if (productThumbnail != null) {
                    addProductThumbnail(product, productThumbnail);
                }
            } catch (Exception e) {
                throw new ProductOperationException("Failed to create product thumbnail " + e.getMessage());
            }

            // add product into database
            try {
                int effNum = productDao.insertProduct(product);
                if (effNum <= 0) {
                    throw new ProductOperationException("Failed to add product");
                }
            } catch (Exception e) {
                throw new ProductOperationException(e.getMessage());
            }

            // add product detail image
            try {
                if (productImgList != null && productImgList.size() > 0) {
                    addProductDetailImage(product, productImgList);
                }
            } catch (Exception e) {
                throw new ProductOperationException(e.getMessage());
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY_ELEMENT);
        }
    }

    private void addProductThumbnail(Product product, ImageHolder productThumbnail) {
        String filePath = PathUtil.getProductThumbnailPath(product.getShop().getShopId());
        String thumbnailPath = ImageUtil.generateThumbnail(productThumbnail, filePath);
        product.setImgAddr(thumbnailPath);
    }

    @Transactional
    public void addProductDetailImage(Product product, List<ImageHolder> productImgList)
            throws ProductOperationException{
        if (product.getProductId() == null) {
            throw new ProductOperationException("Cannot get product Id, store to database might did not finish");
        }
        String filePath = PathUtil.getProductImagePath(product.getShop().getShopId(), product.getProductId());
        List<ProductImg> productImgListToDatabase = new ArrayList<>();
        for (ImageHolder imageHolder : productImgList) {
            try {
                String imgAddress = ImageUtil.generateProductDetailImage(imageHolder, filePath);
                ProductImg productImg = new ProductImg();
                productImg.setImgAddress(imgAddress);
                productImg.setProductId(product.getProductId());
                productImg.setCreateTime(new Date());
                productImg.setLastEditTime(new Date());
                productImgListToDatabase.add(productImg);
            } catch (Exception e) {
                throw new ProductOperationException("Create product detail image failed" + e.getMessage());
            }
        }
        if (productImgListToDatabase.size() > 0) {
            try {
                int effNum = productImgDao.batchInsertProductImg(productImgListToDatabase);
                if (effNum <= 0) {
                    throw new ProductOperationException("Store product detail image failed");
                }
            } catch (Exception e) {
                throw new ProductOperationException("Store product detail image failed" + e.getMessage());
            }
        }
    }
}
