package zuoyang.o2o.enums;

import lombok.Getter;

@Getter
public enum ShopStateEnum {
    CHECK(0, "UnderReivew"),
    OFFLINE(-1, "Illegal Shop"),
    SUCCESS(1, "Operation Succeed"),
    PASS(2, "Passed Review"),
    INNER_ERROR(-1001, "SystemError"),
    NULL_SHOPID(-1002, "ShopId is null"),
    NULL_SHOP(-1003, "Shop Information is null");

    private int state;
    private String stateInfo;

    ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum stateEnum : values()) {
            if (stateEnum.getState()==state) {
                return stateEnum;
            }
        }
        return null;
    }
}
