package zuoyang.o2o.service.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zuoyang.o2o.dto.LocalAuthExecution;
import zuoyang.o2o.entity.LocalAuth;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.enums.LocalAuthStateEnum;
import zuoyang.o2o.service.LocalAuthService;
import zuoyang.o2o.service.PersonInfoService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LocalAuthServiceImplTest {
    @Autowired
    LocalAuthService localAuthService;
    @Autowired
    PersonInfoService personInfoService;

    @Test
    void createNewLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setPassword("1234567");
        localAuth.setUsername("test user 2");
        PersonInfo personInfo = personInfoService.getPersonInfoById(3L);
        localAuth.setPersonInfo(personInfo);
        LocalAuthExecution localAuthExecution = localAuthService.createNewLocalAuth(localAuth);
        assertEquals(0,localAuthExecution.getState());
    }

    @Test
    void getLocalAuthByUsernameAndPassword() {
        String username = "test user 2";
        String password = "1234567";
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPassword(username, password);
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
    void getLocalAuthByUserId() {
        long userId = 3L;
        LocalAuth localAuth = localAuthService.getLocalAuthByUserId(userId);
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
    void updateLocalAuth() {
        long userId = 3L;
        String username = "test user 2";
        String password = "1234567";
        String newPassword = "7654321";
        LocalAuthExecution localAuthExecution = localAuthService.
                updateLocalAuth(userId, username, password, newPassword);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
    }
}