package zuoyang.o2o.service;

import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.exception.ProductOperationException;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    /**
     *
     * @param product
     * @param productThumbnail thumbnail for product
     * @param productImgList detail images for product
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct (Product product, ImageHolder productThumbnail,
                                 List<ImageHolder> productImgList) throws ProductOperationException;

    Product getProductInfo (Long productId);

    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productDetailImageList)
        throws ProductOperationException;
}
