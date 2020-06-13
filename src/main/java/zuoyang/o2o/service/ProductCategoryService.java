package zuoyang.o2o.service;

import zuoyang.o2o.dto.ProductCategoryExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.exception.ProductOperationException;

import java.util.List;

public interface ProductCategoryService {
    /**
     * get a list of product category in a shop
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(Long shopId);

    /**
     *
     * @param productCategoryList
     * @return
     * @throws ProductOperationException
     */
    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
            throws ProductOperationException;

    /**
     *
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductOperationException
     */
    ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId)
            throws ProductOperationException;
}
