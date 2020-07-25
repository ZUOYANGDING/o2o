package zuoyang.o2o.service;

import zuoyang.o2o.dto.LocalAuthExecution;
import zuoyang.o2o.entity.LocalAuth;
import zuoyang.o2o.exception.LocalAuthOperationException;

import java.util.Date;

public interface LocalAuthService {
    LocalAuth getLocalAuthByUsernameAndPassword(String username, String password) throws LocalAuthOperationException;

    LocalAuth getLocalAuthByUserId (long userId) throws LocalAuthOperationException;

    LocalAuthExecution createNewLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    LocalAuthExecution updateLocalAuth(long userId, String username, String password, String newPassword)
            throws LocalAuthOperationException;
}
