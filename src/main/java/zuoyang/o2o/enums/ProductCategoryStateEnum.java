package zuoyang.o2o.enums;

import lombok.Getter;

@Getter
public enum ProductCategoryStateEnum {
    SUCCESS(1, "Operation Succeed"),
    INNER_ERROR(-1001, "Operation Failed"),
    EMPTY_LIST(-1002, "INVALID NUMBER OF ADD");

    private int state;
    private String stateInfo;

    ProductCategoryStateEnum() {}

    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ProductCategoryStateEnum stateOf(int state) {
        for (ProductCategoryStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }
}
