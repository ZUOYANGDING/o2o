package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import zuoyang.o2o.dao.ShopCategoryDao;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ShopCategoryService;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    private final ShopCategoryDao shopCategoryDao;

    public ShopCategoryServiceImpl(ShopCategoryDao shopCategoryDao) {
        this.shopCategoryDao = shopCategoryDao;
    }

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopOperationException {
        try {
            return shopCategoryDao.queryShopCategory(shopCategoryCondition);
        } catch (Exception e) {
            throw new ShopOperationException("Failed to fetch shop category list " + e.getMessage());
        }
    }
}
