package zuoyang.o2o.service.serviceImp;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    /**
     * add thumbnail to image storage and save the path to product
     * @param product
     * @param productThumbnail
     */
    private void addProductThumbnail(Product product, ImageHolder productThumbnail) {
        String filePath = PathUtil.getProductThumbnailPath(product.getShop().getShopId());
        String thumbnailPath = ImageUtil.generateThumbnail(productThumbnail, filePath);
        product.setImgAddr(thumbnailPath);
    }

    /**
     * add product detail images to image storage and save the path to productImg database
     * @param product
     * @param productImgList
     * @throws ProductOperationException
     */
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

    @Override
    public Product getProductInfo(Long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
                                          List<ImageHolder> productDetailImgList) throws ProductOperationException {
        if (product!=null && product.getShop()!=null && product.getShop().getShopId()!=null) {

            // valid check for productId
            Product tempProduct = getProductInfo(product.getProductId());
            if (tempProduct == null) {
                throw new ProductOperationException("Cannot find matched product");
            }
            if (product.getShop().getShopId() != tempProduct.getShop().getShopId()) {
                throw new ProductOperationException("cannot find matched product in this shop");
            }

            // set basic info
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);

            // deal with thumbnail
            if (thumbnail!=null && thumbnail.getImageName()!=null) {
                // delete the old thumbnail if exist
                if (tempProduct.getImgAddr() != null) {
                    try {
                        ImageUtil.deleteFileOrDirectory(tempProduct.getImgAddr());
                    } catch (Exception e) {
                        throw new ProductOperationException("failed to update the thumbnail " + e.getMessage());
                    }
                }

                // setup new thumbnail
                try {
                    addProductThumbnail(product, thumbnail);
                } catch (Exception e) {
                    throw new ProductOperationException("failed to update the thumbnail " + e.getMessage());
                }
            }

            // deal with detail image
            if (productDetailImgList!=null && productDetailImgList.size()>0) {
                // delete the old detail images if exist
                if (tempProduct.getProductImgList() != null) {
                    deleteProductDetailImgList(tempProduct, tempProduct.getProductImgList());
                }

                // setup new product detail images
                try {
                    addProductDetailImage(product, productDetailImgList);
                } catch (Exception e) {
                    throw new ProductOperationException("failed to update detail Image " + e.getMessage());
                }
            }

            // update the product to database
            try {
                int effNum = productDao.updateProduct(product);
                if (effNum <= 0) {
                    throw new ProductOperationException("failed to update product");
                } else {
                    return new ProductExecution(ProductStateEnum.SUCCESS, product);
                }
            } catch (Exception e) {
                throw new ProductOperationException("failed to update product " + e.getMessage());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY_ELEMENT);
        }
    }

    /**
     * delete product detail image from image storage and productImg database
     * @param product
     * @param productImgList
     */
    @Transactional
    public void deleteProductDetailImgList(Product product, List<ProductImg> productImgList) {
        Long productId = product.getProductId();
        Long shopId = product.getShop().getShopId();
        int effNum = productImgDao.deleteProductImgByProductId(productId);
        log.info("Delete product detail image result " + effNum);
        String deleteFilePath = PathUtil.getProductImagePath(shopId, productId);
        ImageUtil.deleteFileOrDirectory(deleteFilePath);
    }
}
