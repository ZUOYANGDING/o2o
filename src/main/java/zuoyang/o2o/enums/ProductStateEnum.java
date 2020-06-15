package zuoyang.o2o.enums;

import lombok.Getter;

@Getter
public enum ProductStateEnum {
    SUCCESS(1, "Operation Success"),
    INNER_ERROR(-1001, "Operation Failed"),
    EMPTY_ELEMENT(-1002, "Empty required element input");

    private int state;
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ProductStateEnum statesOf(int state) {
        for (ProductStateEnum stateEnum : values()) {
            if (state == stateEnum.getState()) {
                return stateEnum;
            }
        }
        return null;
    }
}
