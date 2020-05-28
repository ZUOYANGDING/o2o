package zuoyang.o2o.dao;

import zuoyang.o2o.entity.Shop;

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
}
