package zuoyang.o2o.service;

import org.springframework.stereotype.Service;
import zuoyang.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
