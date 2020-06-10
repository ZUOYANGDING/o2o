package zuoyang.o2o.dao;

import zuoyang.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    List<ProductCategory> queryProductCategoryList(Long shopId);
}
