package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LocalAuth {
    private Long localAuthId;
    private String password;
    private String userName;
    private PersonInfo personInfo;
    private Date createTime;
    private Date lastEditTime;
}
