package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.service.CacheService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AreaServiceImplTest {
    @Autowired
    private AreaServiceImpl areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    void getAreaList() {
//        cacheService.removeCache(areaService.AREA_LIST_KEY);
        List<Area> areaList = areaService.getAreaList();
        assertEquals("Fremont", areaList.get(0).getAreaName());
    }
}