package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import zuoyang.o2o.dao.PersonInfoDao;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    private final PersonInfoDao personInfoDao;

    public PersonInfoServiceImpl(PersonInfoDao personInfoDao) {
        this.personInfoDao = personInfoDao;
    }

    @Override
    public PersonInfo getPersonInfoById (long userId) throws RuntimeException {
        if (userId > 0) {
            try {
                PersonInfo personInfo = personInfoDao.queryPersonInfoById(userId);
                return personInfo;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("illegal userId....");
        }
    }
}
