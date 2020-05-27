package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import zuoyang.o2o.dao.AreaDao;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.service.AreaService;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaDao areaDao;

    public AreaServiceImpl(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
