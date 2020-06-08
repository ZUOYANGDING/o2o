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
import zuoyang.o2o.util.PageCalculate;
import zuoyang.o2o.util.PathUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    private final ShopDao shopDao;

    public ShopServiceImpl(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,
                                 String imgFileName) throws ShopOperationException{
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
                if (shopImgInputStream != null) {
                    try {
                        storeShopImage(shop, shopImgInputStream, imgFileName);
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

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream,
                                    String fileName) throws ShopOperationException {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        if (shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
        }
        try {
            // check if need to update shopImg or not
            if (shopImgInputStream != null && fileName != null) {
                Shop originShop = getShopById(shop.getShopId());
                if (originShop.getShopImg()!=null) {
                    //delete the old shopImg
                    ImageUtil.deleteFileOrDirectory(originShop.getShopImg());
                }
                //add new shopImage
                storeShopImage(shop, shopImgInputStream, fileName);
            }

            //update shop info
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum<=0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                Shop updatedShop = getShopById(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS, updatedShop);
            }
        } catch (ShopOperationException e) {
            throw new ShopOperationException(e.getMessage());
        }
    }


    @Override
    public Shop getShopById(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        // Must have init dataset for shop in database
        // or cause empty return but not error in code
        ShopExecution shopExecution;
        if (shopList != null) {
            shopExecution = new ShopExecution(ShopStateEnum.SUCCESS, shopList, count);
        } else {
            shopExecution = new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
        return shopExecution;
    }

    /**
     * store shop image
     * @param shop
     * @param shopImgInputStream
     * @param imgFileName
     */
    public void storeShopImage(Shop shop, InputStream shopImgInputStream, String imgFileName) {
        String shopImagePath = PathUtil.getShopImagePath(shop.getShopId());
        String shopImageRelativePath = ImageUtil.generateThumbnail(shopImgInputStream, imgFileName, shopImagePath);
        shop.setShopImg(shopImageRelativePath);
    }
}
