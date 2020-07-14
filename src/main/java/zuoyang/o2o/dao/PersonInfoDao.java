package zuoyang.o2o.dao;

import zuoyang.o2o.entity.PersonInfo;

public interface PersonInfoDao {
    /**
     * query PersonInfo by userId
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * insert new PersonInfo
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);
}
