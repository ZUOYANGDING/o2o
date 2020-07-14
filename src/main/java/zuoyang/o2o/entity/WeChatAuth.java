package zuoyang.o2o.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WeChatAuth {
    private Long wechatAuthId;
    private String openId;
    private PersonInfo personInfo;
    private Date createTime;
}
