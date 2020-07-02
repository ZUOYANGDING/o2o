package zuoyang.o2o.service;

import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.exception.ShopOperationException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ShopService {
    /**
     *
     * @param shop
     * @param thumbnail
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

    /**
     *Cannot modify the shop owner and shop category
     * @param shop
     * @param thumbnail
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

    /**
     * Return shopInfo for shop modify. Without owner info. Mask shopCategory, only return category name and id.
     * @param shopId
     * @return
     */
    Shop getShopById(Long shopId);

    /**
     * return shop list by page separation require (limit (rowIndex, pageSize), where pageIndex transfer
     * to rowIndex by calculateRowIndex in Util)
     * @param shopCondition filter info from frontend
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException;
}
