package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.Product;

import java.util.List;

public interface ProductDao {
    /**
     * get product by product id
     * @param productId
     * @return
     */
    Product queryProductByProductId(Long productId);

    /**
     * create new product
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * delete product in a shop by product id, and shop id
     * @param ProductId
     * @param shopId
     * @return
     */
    int deleteProductByProductId(@Param("productId") Long ProductId, @Param("shopId") Long shopId);

    /**
     * update product info by product
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * get list of product by matching members in product
     * @param product
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product product,
                                   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    /**
     * get number of results of queryProductList
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * detach the product and product category, called when delete product category
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(Long productCategoryId);

}
