package zuoyang.o2o.service.serviceImp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.dao.PersonInfoDao;
import zuoyang.o2o.dto.WeChatAuthExecution;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.WeChatAuth;
import zuoyang.o2o.enums.WeChatAuthStateEnum;
import zuoyang.o2o.service.WeChatAuthService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WeChatAuthServiceImplTest {
    @Autowired
    WeChatAuthService weChatAuthService;
    @Autowired
    PersonInfoDao personInfoDao;

    @Test
    @Order(1)
    void getWeChatAuthByOpenId() {
        String openId = "123456";
        WeChatAuth weChatAuth = weChatAuthService.getWeChatAuthByOpenId(openId);
        assertEquals(2, weChatAuth.getPersonInfo().getUserId());
    }

    @Test
    @Order(2)
    void addWeChatAuthWithOutExistingPersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("test name 1");
        personInfo.setEmail("test email");
        personInfo.setUserType(1);
        personInfo.setProfileImg("test image");
        personInfo.setGender("M");
        WeChatAuth weChatAuth = new WeChatAuth();
        weChatAuth.setOpenId("654321");
        weChatAuth.setPersonInfo(personInfo);
        WeChatAuthExecution weChatAuthExecution = weChatAuthService.addWeChatAuth(weChatAuth);
        assertEquals(WeChatAuthStateEnum.SUCCESS.getState(), weChatAuthExecution.getState());
        PersonInfo personInfo1 = weChatAuthExecution.getWeChatAuth().getPersonInfo();
        System.out.println("userId: " + personInfo1.getUserId());
        System.out.println("name: " + personInfo1.getName());
        System.out.println("profileImg: " + personInfo1.getEmail());
        System.out.println("gender: " + personInfo1.getGender());
        System.out.println("User status :" + personInfo1.getUserStatus());
        System.out.println("User type: " + personInfo1.getUserType());
        System.out.println("create time: " + personInfo1.getCreateTime());
        System.out.println("last edit time: " + personInfo1.getLastEditTime());
    }

    @Test
    @Order(2)
    void addWeChatAuthWithExistingPersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("test name 2");
        personInfo.setEmail("test email 2");
        personInfo.setUserType(1);
        personInfo.setProfileImg("test image 2");
        personInfo.setGender("F");
        personInfo.setUserStatus(1);
        personInfo.setLastEditTime(new Date());
        personInfo.setCreateTime(new Date());
        personInfoDao.insertPersonInfo(personInfo);
        WeChatAuth weChatAuth = new WeChatAuth();
        weChatAuth.setOpenId("abcdefg");
        weChatAuth.setPersonInfo(personInfo);
        WeChatAuthExecution weChatAuthExecution = weChatAuthService.addWeChatAuth(weChatAuth);
        assertEquals(WeChatAuthStateEnum.SUCCESS.getState(), weChatAuthExecution.getState());
        PersonInfo personInfo1 = weChatAuthExecution.getWeChatAuth().getPersonInfo();
        System.out.println("userId: " + personInfo1.getUserId());
        System.out.println("name: " + personInfo1.getName());
        System.out.println("profileImg: " + personInfo1.getEmail());
        System.out.println("gender: " + personInfo1.getGender());
        System.out.println("User status :" + personInfo1.getUserStatus());
        System.out.println("User type: " + personInfo1.getUserType());
        System.out.println("create time: " + personInfo1.getCreateTime());
        System.out.println("last edit time: " + personInfo1.getLastEditTime());
    }
}