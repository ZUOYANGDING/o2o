package zuoyang.o2o.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShopCategory {
    private Long shopCategoryId;
//    null for super category, id for subcategory
    private Long parentId;
    private String shopCategoryName;
    private String shopCategoryDescription;
    private String shopCategoryImg;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;

}
