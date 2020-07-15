package zuoyang.o2o.enums;

import lombok.Getter;

@Getter
public enum WeChatAuthStateEnum {
    LOGIN_FAIL(-1, "No matching user, Failed to login"),
    SUCCESS(0, "Login Successfully"),
    NULL_AUTH_INFO(-1006, "Miss required login info");

    private int state;
    private String stateInfo;

    WeChatAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static WeChatAuthStateEnum stateOf(int state) {
        for (WeChatAuthStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

}
