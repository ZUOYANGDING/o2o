package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.Product;

import java.util.List;

public interface ProductDao {
    Product queryProductByProductId(Long productId);

    int insertProduct(Product product);

    int deleteProductByProductId(@Param("productId") Long ProductId, @Param("shopId") Long shopId);

    int updateProduct(Product product);

    List<Product> queryProductList(@Param("productCondition") Product product,
                                   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryProductCount(@Param("productCondition") Product productCondition);

}
