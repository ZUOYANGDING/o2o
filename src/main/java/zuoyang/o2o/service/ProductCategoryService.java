package zuoyang.o2o.service;

import zuoyang.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    /**
     * get a list of product category in a shop
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(Long shopId);
}
