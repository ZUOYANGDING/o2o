package zuoyang.o2o.service.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuoyang.o2o.cache.JedisUtil;
import zuoyang.o2o.dao.ShopCategoryDao;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ShopCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShopCategoryServiceImpl implements ShopCategoryService {
    private final ShopCategoryDao shopCategoryDao;
    private final JedisUtil.Keys keys;
    private final JedisUtil.Strings strings;

    public ShopCategoryServiceImpl(ShopCategoryDao shopCategoryDao, JedisUtil.Keys keys, JedisUtil.Strings strings) {
        this.shopCategoryDao = shopCategoryDao;
        this.keys = keys;
        this.strings = strings;
    }

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopOperationException {
        String key = SHOP_CATEGORY_LIST_KEY;
        ObjectMapper objectMapper = new ObjectMapper();
        List<ShopCategory> shopCategoryList = null;
        if (shopCategoryCondition == null) {
            // get all root shop categories
            key += "_rootCategories";
        } else if (shopCategoryCondition.getParent()!=null &&
                shopCategoryCondition.getParent().getShopCategoryId()!=null) {
            // get all categories under this parent category
            key += "_parent_" + shopCategoryCondition.getParent().getShopCategoryId();
        } else {
            // get all sub level categories (under root level)
            key += "_allSubLevelCategories";
        }
        if (keys.exists(key)) {
            log.debug("get shop list from redis........");
            String shopCategoryString = strings.getValue(key);
            JavaType javaType = objectMapper.getTypeFactory().
                    constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = objectMapper.readValue(shopCategoryString, javaType);
            } catch (JsonProcessingException e) {
                log.error("get shop list from redis failed...");
                throw new ShopOperationException(e.getMessage());
            }
        } else {
            log.debug("get shop list from mysql.....");
            try {
                shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
                String shopCategoryString = objectMapper.writeValueAsString(shopCategoryList);
                strings.setPair(key, shopCategoryString);
            } catch (JsonProcessingException e) {
                log.error("get shop list from mysql failed");
                throw new ShopOperationException(e.getMessage());
            }

        }
        return shopCategoryList;
    }
}
