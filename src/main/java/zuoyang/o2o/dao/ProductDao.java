package zuoyang.o2o.dao;

import zuoyang.o2o.entity.Product;

public interface ProductDao {
    Product queryProductByProductId(Long productId);

    int insertProduct(Product product);

    int deleteProductByProductId(Long ProductId);
}
