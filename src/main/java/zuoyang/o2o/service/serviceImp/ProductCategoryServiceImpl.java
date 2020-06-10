package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import zuoyang.o2o.dao.ProductCategoryDao;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.service.ProductCategoryService;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryDao productCategoryDao;

    public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }
}
