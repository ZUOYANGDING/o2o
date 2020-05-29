package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ShopStateEnum;

import java.util.List;

@Getter
@Setter
public class ShopExecution {
    // state code
    private int state;
    // state information
    private String stateInfo;
    // number of shops
    private int count;
    // for shop edit
    private Shop shop;
    // for get list of shop
    private List<Shop> shopList;

    public ShopExecution(){
    }

    /**
     * constructor for operation failed
     * @param stateEnum
     */
    public ShopExecution(ShopStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * constructor for operation succeed
     * @param stateEnum
     * @param shop
     */
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    /**
     * constructor for operation succeed
     * @param stateEnum
     * @param shopList
     */
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }
}
