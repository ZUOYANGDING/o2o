package zuoyang.o2o.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HeadLine {
    private Long lindId;
    private String lingImg;
    private String lineLink;
    private String lineName;
    //    larger will be put in front of headline queue
    private Integer priority;
    //    0 for ban, 1 for active;
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;
}
