package zuoyang.o2o.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
class LocalAuthDaoTest {
    @Autowired
    LocalAuthDao localAuthDao;
    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    void insertLocalAuth () {
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2L);
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername("test user 1");
        localAuth.setPassword("123456");
        localAuth.setPersonInfo(personInfo);
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        int effNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1, effNum);
    }

    @Test
    void queryLocalAuthByUserNameAndPassWord() {
        String userName = "test user 1";
        String password = "123456";
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserNameAndPassWord(userName, password);
        assertNotNull(localAuth);
        PersonInfo personInfo = localAuth.getPersonInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String personInfoString = objectMapper.writeValueAsString(personInfo);
            System.out.println(personInfoString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void queryLocalAuthByUserId() {
        long userId = 2;
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(userId);
        assertNotNull(localAuth);
        ObjectMapper objectMapper = new ObjectMapper();
        PersonInfo personInfo = localAuth.getPersonInfo();
        try {
            String personInfoString = objectMapper.writeValueAsString(personInfo);
            System.out.println(personInfoString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Test
    void updateLocalAuth() {
        long userId = 2L;
        String username = "test user 1";
        String password = "6543210";
        String newPassword = "123456";
        Date lastEditTime = new Date();
        int effNum = localAuthDao.updateLocalAuth(userId, username, password, newPassword, lastEditTime);
        assertEquals(1, effNum);
    }
}