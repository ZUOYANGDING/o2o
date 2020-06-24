package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineDao {
    List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);
}
