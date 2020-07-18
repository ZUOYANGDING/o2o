package zuoyang.o2o.service;

import zuoyang.o2o.entity.Area;
import zuoyang.o2o.exception.AreaOperationException;

import java.util.List;

public interface AreaService {
    // Key for redis cache
    public static final String AREA_LIST_KEY = "areaList";
    List<Area> getAreaList() throws AreaOperationException;
}
