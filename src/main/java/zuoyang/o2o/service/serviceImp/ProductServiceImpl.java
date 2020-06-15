package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.ProductDao;
import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.enums.ProductStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductService;
import zuoyang.o2o.util.ImageUtil;
import zuoyang.o2o.util.PathUtil;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
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
            if (productThumbnail != null) {
                addProductThumbnail(product, productThumbnail);
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
            if (productImgList!=null && productImgList.size()>0) {
                addProductDetailImage(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY_ELEMENT);
        }
    }

    private void addProductThumbnail(Product product, ImageHolder productThumbnail) {
        String filePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailPath = ImageUtil.generateThumbnailForProduct(productThumbnail, filePath);
        product.setImgAddr(thumbnailPath);
    }

    private void addProductDetailImage(Product product, List<ImageHolder> productImgList) {

    }
}
