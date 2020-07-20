package zuoyang.o2o.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.LocalAuth;
import zuoyang.o2o.entity.PersonInfo;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocalAuthDaoTest {
    @Autowired
    LocalAuthDao localAuthDao;
    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    @Order(1)
    void insertLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername("test uesrname_1");
        localAuth.setPassword("123456");
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2);
        localAuth.setPersonInfo(personInfo);
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        int effNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1, effNum);
    }
}