package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.LocalAuth;

import java.util.Date;

public interface LocalAuthDao {
    LocalAuth queryLocalAuthByUserNameAndPassWord(@Param("username") String username,
                                                  @Param("password") String password);

    LocalAuth queryLocalAuthByUserId(@Param("userId") long userId);

    int insertLocalAuth(LocalAuth localAuth);

    int updateLocalAuth(@Param("userId") Long userId, @Param("username") String username,
                        @Param("password") String password, @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}
