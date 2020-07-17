package zuoyang.o2o.dao;

import org.junit.jupiter.api.BeforeEach;
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
class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    void queryArea() {
        List<Area> areaList = areaDao.queryArea();
        assertEquals(4, areaList.size());
    }
}