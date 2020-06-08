package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryDao {
    // @param applied when the input param is a object or in Map style
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
