package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;
import zuoyang.o2o.enums.LocalAuthStateEnum;

@Getter
@Setter
public class LocalAuthExecution {
    private int state;
    private String stateInfo;

    public LocalAuthExecution(){};

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
    }

}
