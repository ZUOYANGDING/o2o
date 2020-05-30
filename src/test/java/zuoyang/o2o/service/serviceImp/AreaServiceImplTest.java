package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.Area;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AreaServiceImplTest {
    @Autowired
    private AreaServiceImpl areaService;

    @Test
    void getAreaList() {
        List<Area> areaList = areaService.getAreaList();
        assertEquals("Fremont", areaList.get(0).getAreaName());
    }
}