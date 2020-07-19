package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.HeadLine;
import zuoyang.o2o.service.CacheService;
import zuoyang.o2o.service.HeadLineService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HeadLineServiceImplTest {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private CacheService cacheService;

    @Test
    @Order(1)
    void getHeadLineListTest() {
//        cacheService.removeCache(headLineService.HEAD_LINE_KEY);
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        List<HeadLine> headLineList = headLineService.getHeadLineList(headLine);
        assertEquals(3, headLineList.size());
    }
}