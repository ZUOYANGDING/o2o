package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
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

    /**
     *
     * @param productCategoryId
     * @param ShopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") Long productCategoryId,
                              @Param("shopId") Long ShopId);
}
