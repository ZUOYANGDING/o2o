package zuoyang.o2o.service;

import zuoyang.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(Long shopId);
}
