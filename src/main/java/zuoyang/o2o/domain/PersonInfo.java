package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PersonInfo {
    private Long userId;
    private String userName;
    private String profileImg;
    private String email;
    private String gender;
//    0 for ban, 1 for active
    private Integer userStatus;
//    1 for customer, 2 for seller, 3 for super
    private Integer userType;
    private Date createTime;
    private Date lastEditTime;

}
