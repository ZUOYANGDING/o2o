package zuoyang.o2o.service.serviceImp;

import org.springframework.stereotype.Service;
import zuoyang.o2o.dao.HeadLineDao;
import zuoyang.o2o.entity.HeadLine;
import zuoyang.o2o.exception.HeadLineOperationException;
import zuoyang.o2o.service.HeadLineService;

import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    private final HeadLineDao headLineDao;

    public HeadLineServiceImpl(HeadLineDao headLineDao) {
        this.headLineDao = headLineDao;
    }

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLine) throws HeadLineOperationException {
        try {
            List<HeadLine> headLineList = headLineDao.queryHeadLineList(headLine);
            return headLineList;
        } catch (Exception e) {
            throw new HeadLineOperationException("failed the fetch headline list " + e.getMessage());
        }
    }
}
