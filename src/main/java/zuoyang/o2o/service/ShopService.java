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
     *
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;

    /**
     *
     * @param shopId
     * @return
     */
    Shop getShopById(Long shopId);
}
