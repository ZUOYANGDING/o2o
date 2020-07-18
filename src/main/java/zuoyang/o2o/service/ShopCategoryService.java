package zuoyang.o2o.service;

import org.springframework.stereotype.Service;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.exception.ShopOperationException;

import java.util.List;

public interface ShopCategoryService {
    public static final String SHOP_CATEGORY_LIST_KEY = "shopCategoryList";
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopOperationException;
}
