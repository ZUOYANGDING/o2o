package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.Shop;

import java.util.List;

public interface ShopDao {
    /**
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     *
     * @param shopId
     * @return
     */
    Shop queryByShopId(Long shopId);

    /**
     * page separated search, support: elastic search by shopName, search by shop condition,
     * search by shop category, search by areaId, search by shop owner
     * @param shopCondition
     * @param rowIndex offset of the first line
     * @param pageSize num of rows return
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     *
     * @param shopCondition
     * @return total number of results returned by queryShopList
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
