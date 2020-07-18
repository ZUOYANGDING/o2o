package zuoyang.o2o.service;

import zuoyang.o2o.entity.HeadLine;
import zuoyang.o2o.exception.HeadLineOperationException;

import java.util.List;

public interface HeadLineService {
    static final String HEAD_LINE_KEY = "headlineList";
    List<HeadLine> getHeadLineList(HeadLine headLine) throws HeadLineOperationException;
}
