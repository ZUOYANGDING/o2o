package zuoyang.o2o.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.entity.PersonInfo;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonInfoDaoTest {
    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    @Order(1)
    void insertPersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("test nick name");
        personInfo.setProfileImg("test profile image");
        personInfo.setEmail("test email");
        personInfo.setGender("m");
        personInfo.setUserStatus(1);
        personInfo.setUserType(2);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        int effNum = personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1, effNum);
    }

    @Test
    @Order(2)
    void queryPersonInfoById() {
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2L);
        assertEquals("test nick name", personInfo.getName());
        assertEquals("test profile image", personInfo.getProfileImg());
        assertEquals("test email", personInfo.getEmail());
        assertEquals("m", personInfo.getGender());
        assertEquals(1, personInfo.getUserStatus());
        assertEquals(2, personInfo.getUserType());
        System.out.println(personInfo.getCreateTime());
        System.out.println(personInfo.getLastEditTime());
    }
}