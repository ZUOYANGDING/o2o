package zuoyang.o2o.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WeChatAuth {
    private Long wechatAuthId;
    private Long openId;
    private PersonInfo personInfo;
    private Date createTime;
}
