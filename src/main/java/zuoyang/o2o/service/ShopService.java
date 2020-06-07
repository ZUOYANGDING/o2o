package zuoyang.o2o.service;

import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.exception.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    /**
     *
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;

    /**
     *Cannot modify the shop owner and shop category
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;

    /**
     *Return shopInfo for shop modify. Without owner info. Mask shopCategory, only return category name and id.
     * @param shopId
     * @return
     */
    Shop getShopById(Long shopId);
}
