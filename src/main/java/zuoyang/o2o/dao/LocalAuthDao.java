package zuoyang.o2o.dao;

import org.apache.ibatis.annotations.Param;
import zuoyang.o2o.entity.LocalAuth;

import java.util.Date;

public interface LocalAuthDao {
    LocalAuth queryLocalByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    int insertLocalAuth(LocalAuth localAuth);

    int updateLocalAuth(@Param("userId") long userId, @Param("username") String username,
                        @Param("password") String password, @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}
