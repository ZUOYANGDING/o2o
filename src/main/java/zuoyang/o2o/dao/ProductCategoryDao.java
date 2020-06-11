package zuoyang.o2o.dao;

import zuoyang.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    /**
     *
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     * insert product category list
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
}
