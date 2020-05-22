package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WeChatAuth {
    private Long weChatAuthId;
    private Long openId;
    private PersonInfo personInfo;
    private Date createTime;
}
