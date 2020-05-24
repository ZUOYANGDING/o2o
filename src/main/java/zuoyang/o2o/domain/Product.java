package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Product {
    private Long productId;
    private String productName;
//    default img
    private String productImg;
    private String productDesc;
    private String normalPrice;
    private String promotePrice;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
//    0 for ban, 1 for active
    private Integer enableStatus;

    private ProductCategory productCategory;
    private List<ProductImg> productImgList;
    private Shop shop;
}
