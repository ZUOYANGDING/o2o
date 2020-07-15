package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;
import zuoyang.o2o.entity.WeChatAuth;
import zuoyang.o2o.enums.WeChatAuthStateEnum;

import java.util.List;

@Getter
@Setter
public class WeChatAuthExecution {
    private int state;
    private String stateInfo;
    private WeChatAuth weChatAuth;
    private List<WeChatAuth> weChatAuthList;
    public WeChatAuthExecution() {}

    public WeChatAuthExecution(WeChatAuthStateEnum weChatAuthStateEnum) {
        this.state = weChatAuthStateEnum.getState();
        this.stateInfo = weChatAuthStateEnum.getStateInfo();
    }

    public WeChatAuthExecution(WeChatAuthStateEnum weChatAuthStateEnum, WeChatAuth weChatAuth) {
        this.state = weChatAuthStateEnum.getState();
        this.stateInfo = weChatAuthStateEnum.getStateInfo();
        this.weChatAuth = weChatAuth;
    }

    public WeChatAuthExecution(WeChatAuthStateEnum weChatAuthStateEnum, List<WeChatAuth> weChatAuthList) {
        this.state = weChatAuthStateEnum.getState();
        this.stateInfo = weChatAuthStateEnum.getStateInfo();
        this.weChatAuthList = weChatAuthList;
    }
}
