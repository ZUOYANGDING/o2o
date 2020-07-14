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
import zuoyang.o2o.entity.WeChatAuth;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WeChatAuthDaoTest {
    @Autowired
    WeChatAuthDao weChatAuthDao;
    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    @Order(1)
    void insertWeChatAuth() {
        WeChatAuth weChatAuth = new WeChatAuth();
        weChatAuth.setOpenId("123456");
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2L);
        weChatAuth.setPersonInfo(personInfo);
        weChatAuth.setCreateTime(new Date());
        int effNum = weChatAuthDao.insertWeChatAuth(weChatAuth);
        assertEquals(1, effNum);
    }


    @Test
    @Order(2)
    void queryWeChatAuthByOpenId() {
        WeChatAuth weChatAuth = weChatAuthDao.queryWeChatAuthByOpenId("123456");
        assertEquals(2, weChatAuth.getWechatAuthId());
        assertEquals("123456", weChatAuth.getOpenId());
        assertEquals("test nick name", weChatAuth.getPersonInfo().getName());
        assertEquals("test profile image", weChatAuth.getPersonInfo().getProfileImg());
        assertEquals("test email", weChatAuth.getPersonInfo().getEmail());
        assertEquals("m", weChatAuth.getPersonInfo().getGender());
        assertEquals(1, weChatAuth.getPersonInfo().getUserStatus());
        assertEquals(2, weChatAuth.getPersonInfo().getUserType());
        System.out.println(weChatAuth.getPersonInfo().getCreateTime());
        System.out.println(weChatAuth.getPersonInfo().getLastEditTime());
        System.out.print(weChatAuth.getCreateTime());
    }
}