package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductImg {
    private Long productImgId;
    private String imgAddress;
    private String imgDesc;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
    private Long productId;
}
