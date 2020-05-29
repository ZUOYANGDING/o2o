package zuoyang.o2o.service;

import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    ShopExecution addShop(Shop shop, File shopImg);
}
