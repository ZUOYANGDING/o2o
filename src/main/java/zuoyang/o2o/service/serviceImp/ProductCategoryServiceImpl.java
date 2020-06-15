package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.ProductCategoryDao;
import zuoyang.o2o.dto.ProductCategoryExecution;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.enums.ProductCategoryStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
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
            //TODO dispatch the connection between product and productCategoryId
        try {
            int effNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effNum <= 0) {
                return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductOperationException("Product category delete failed: " + e.getMessage());
        }
    }
}
