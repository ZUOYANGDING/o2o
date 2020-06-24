package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.ProductCategoryDao;
import zuoyang.o2o.dao.ProductDao;
import zuoyang.o2o.dto.ProductCategoryExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.enums.ProductCategoryStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductCategoryService;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryDao productCategoryDao;
    private final ProductDao productDao;

    public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao, ProductDao productDao) {
        this.productCategoryDao = productCategoryDao;
        this.productDao = productDao;
    }

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
            throws ProductOperationException {
        if (productCategoryList == null || productCategoryList.size()==0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        } else {
            try {
                int effNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effNum <= 0 || effNum!=productCategoryList.size()) {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
                } else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductOperationException("Product category insert List error: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId)
            throws ProductOperationException {
        try {
            int effNum = productDao.updateProductCategoryToNull(productCategoryId);
            if (effNum < 0) {
                throw new ProductOperationException("failed to delete product category");
            }
        } catch (Exception e) {
            throw new ProductOperationException("product category delete failed " + e.getMessage());
        }
        try {
            int effNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effNum <= 0) {
                throw new ProductOperationException("Failed to delete the product category");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductOperationException("Product category delete failed: " + e.getMessage());
        }
    }
}
