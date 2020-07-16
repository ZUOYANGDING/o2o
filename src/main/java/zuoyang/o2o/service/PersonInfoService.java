package zuoyang.o2o.service;

import zuoyang.o2o.entity.PersonInfo;

public interface PersonInfoService {
    /**
     * Get personInfo by user ID
     * @param userId
     * @return
     * @throws RuntimeException
     */
    PersonInfo getPersonInfoById (long userId) throws RuntimeException;


}
