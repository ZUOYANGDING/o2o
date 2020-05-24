package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Shop {
    private Long shopId;
    private String shopName;
    private String shopDesc;
    private String shopAddress;
    private String phoneNumber;
    private String shopImg;
    private Integer priority;
//    -1 for ban, 0 for review, 1 for active
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;
    private String advice;
    private Area area;
    private PersonInfo personInfo;
    private ShopCategory shopCategory;
}
