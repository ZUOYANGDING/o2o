package zuoyang.o2o.service.serviceImp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.PersonInfoDao;
import zuoyang.o2o.dao.WeChatAuthDao;
import zuoyang.o2o.dto.WeChatAuthExecution;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.WeChatAuth;
import zuoyang.o2o.enums.WeChatAuthStateEnum;
import zuoyang.o2o.exception.WeChatAuthOperationException;
import zuoyang.o2o.service.WeChatAuthService;

import java.util.Date;

@Service
@Slf4j
public class WeChatAuthServiceImpl implements WeChatAuthService {
    private final WeChatAuthDao weChatAuthDao;
    private final PersonInfoDao personInfoDao;

    public WeChatAuthServiceImpl(WeChatAuthDao weChatAuthDao, PersonInfoDao personInfoDao) {
        this.weChatAuthDao = weChatAuthDao;
        this.personInfoDao = personInfoDao;
    }

    @Override
    public WeChatAuth getWeChatAuthByOpenId(String openId) throws WeChatAuthOperationException{
        try {
            WeChatAuth weChatAuth = weChatAuthDao.queryWeChatAuthByOpenId(openId);
            if (weChatAuth != null) {
                return weChatAuth;
            } else {
                throw new WeChatAuthOperationException("failed to get weChatAuth by openId");
            }
        } catch (Exception e) {
            throw new WeChatAuthOperationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public WeChatAuthExecution addWeChatAuth(WeChatAuth weChatAuth) {
        if (weChatAuth==null || weChatAuth.getOpenId()==null) {
            return new WeChatAuthExecution(WeChatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            weChatAuth.setCreateTime(new Date());

            // create personInfo for new user
            if (weChatAuth.getPersonInfo()!=null && weChatAuth.getPersonInfo().getUserId()==null) {
                try {
                    PersonInfo updatePersonInfo = weChatAuth.getPersonInfo();
                    updatePersonInfo.setUserStatus(1);
                    updatePersonInfo.setLastEditTime(new Date());
                    updatePersonInfo.setCreateTime(new Date());
                    int effNum = personInfoDao.insertPersonInfo(updatePersonInfo);
                    if (effNum <= 0) {
                        throw new WeChatAuthOperationException("create new personInfo failed");
                    } else {
                        log.debug("personInfo.userId: " + updatePersonInfo.getUserId());
                        weChatAuth.setPersonInfo(updatePersonInfo);
                    }
                } catch (Exception e) {
                    log.error("insert personInfo error " + e.getMessage());
                    throw new WeChatAuthOperationException("create new person failed");
                }
            }

            // create weChatAuth
            int effNum = weChatAuthDao.insertWeChatAuth(weChatAuth);
            if (effNum <= 0) {
                throw new WeChatAuthOperationException("create new weChatAuth failed");
            } else {
                return new WeChatAuthExecution(WeChatAuthStateEnum.SUCCESS, weChatAuth);
            }

        } catch (Exception e) {
            log.error("insert weChatAuth failed");
            throw new WeChatAuthOperationException("create new weChatAuth failed");
        }
    }
}
