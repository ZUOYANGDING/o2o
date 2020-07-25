package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuoyang.o2o.dao.LocalAuthDao;
import zuoyang.o2o.dto.LocalAuthExecution;
import zuoyang.o2o.entity.LocalAuth;
import zuoyang.o2o.enums.LocalAuthStateEnum;
import zuoyang.o2o.exception.LocalAuthOperationException;
import zuoyang.o2o.service.LocalAuthService;
import zuoyang.o2o.util.MD5Util;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    private final LocalAuthDao localAuthDao;

    public LocalAuthServiceImpl(LocalAuthDao localAuthDao) {
        this.localAuthDao = localAuthDao;
    }

    @Override
    public LocalAuth getLocalAuthByUsernameAndPassword(String username, String password)
            throws LocalAuthOperationException {
        if (username!=null && password!=null) {
            try {
                String pw = MD5Util.encryptByMD5(password);
                LocalAuth localAuth = localAuthDao.queryLocalAuthByUserNameAndPassWord(username, pw);
                return localAuth;
            } catch (Exception e) {
                throw new LocalAuthOperationException(e.getMessage());
            }
        } else {
            throw new LocalAuthOperationException("both username and password are required");
        }
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) throws LocalAuthOperationException {
        if (userId > -1) {
            try {
                LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(userId);
                return localAuth;
            } catch (Exception e) {
                throw new LocalAuthOperationException(e.getMessage());
            }
        } else {
            throw new LocalAuthOperationException("Invalid userId");
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution createNewLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        LocalAuthExecution localAuthExecution;
        if (localAuth!=null && localAuth.getPassword()!=null && localAuth.getUsername()!=null &&
                localAuth.getPersonInfo()!=null && localAuth.getPersonInfo().getUserId()>-1) {
            try {
                LocalAuth tempLocalAuth = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
                // check if this account has already been bound with weChat Auth
                if (tempLocalAuth != null) {
                    localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.BIND_ALREADY);
                } else {
                    localAuth.setCreateTime(new Date());
                    localAuth.setLastEditTime(new Date());
                    localAuth.setPassword(MD5Util.encryptByMD5(localAuth.getPassword()));
                    int effNum = localAuthDao.insertLocalAuth(localAuth);
                    if (effNum > 0) {
                        localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
                    } else {
                        localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.INNER_ERROR);
                    }
                }
            } catch (Exception e) {
                throw new LocalAuthOperationException(e.getMessage());
            }
        } else {
            localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.MISSING_ARGS);
        }
        return localAuthExecution;
    }

    @Override
    @Transactional
    public LocalAuthExecution updateLocalAuth(long userId, String username, String password,
                                              String newPassword) throws LocalAuthOperationException{
        LocalAuthExecution localAuthExecution;
        if (userId>-1 && username!=null && password!=null && newPassword!=null) {
            if (newPassword.equals(password)) {
                localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.INVALID_ARGS);
            } else {
                try {
                    String pw = MD5Util.encryptByMD5(password);
                    String npw = MD5Util.encryptByMD5(newPassword);
                    int effNum = localAuthDao.updateLocalAuth(userId, username, pw, npw, new Date());
                    if (effNum > 0) {
                        localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
                    } else {
                        throw new LocalAuthOperationException("update password failed");
                    }
                } catch (Exception e) {
                    throw new LocalAuthOperationException("update password failed " + e.getMessage());
                }
            }
        } else {
            localAuthExecution = new LocalAuthExecution(LocalAuthStateEnum.MISSING_ARGS);
        }
        return localAuthExecution;
    }
}
