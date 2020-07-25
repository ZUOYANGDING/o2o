package zuoyang.o2o.enums;

import lombok.Getter;

@Getter
public enum LocalAuthStateEnum {
    SUCCESS(1, "Operation Succeed"),
    BIND_ALREADY(0, "Account has already been bound"),
    INNER_ERROR(-1001, "Operation Failed"),
    MISSING_ARGS(-1002, "Miss Required Arguments"),
    INVALID_ARGS(-1003, "Invalid arguments");

    private int state;
    private String stateInfo;

    LocalAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static LocalAuthStateEnum stateOf(int state) {
        for (LocalAuthStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }
}
