package zuoyang.o2o.service.serviceImp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.ShopDao;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ShopStateEnum;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ShopService;
import zuoyang.o2o.util.ImageUtil;
import zuoyang.o2o.util.PathUtil;

import java.io.File;
import java.util.Date;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    private final ShopDao shopDao;

    public ShopServiceImpl(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
//        if (shop.getShopId() == null) {
//            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
//        }
        try {
            // new shop is under review
            shop.setEnableStatus(0);
            // set time for create the new shop
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effNumberShop = shopDao.insertShop(shop);
            if (effNumberShop <= 0) {
                // use runtime exception for transactional
                throw new ShopOperationException("create shop failed");
            } else {
                // store shop Image
                if (shopImg != null) {
                    try {
                        storeShopImage(shop, shopImg);
                    } catch (Exception e) {
                        log.error("store shop image failed: " + e.getMessage());
                        throw new ShopOperationException("store shop image failed: " + e.getMessage());
                    }
                    int effNumberImage = shopDao.updateShop(shop);
                    if (effNumberImage <= 0) {
                        log.error("add shop image failed");
                        throw new ShopOperationException("add shop image failed");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ShopOperationException("add shop failed: " +  e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /**
     * store shop image
     * @param shop
     * @param shopImage
     */
    public void storeShopImage(Shop shop, File shopImage) {
        String shopImagePath = PathUtil.getShopImagePath(shop.getShopId());
        String shopImageRelativePath = ImageUtil.generateThumbnail(shopImage, shopImagePath);
        shop.setShopImg(shopImageRelativePath);
    }
}
