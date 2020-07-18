package zuoyang.o2o.service.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuoyang.o2o.cache.JedisUtil;
import zuoyang.o2o.dao.HeadLineDao;
import zuoyang.o2o.entity.HeadLine;
import zuoyang.o2o.exception.HeadLineOperationException;
import zuoyang.o2o.service.HeadLineService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HeadLineServiceImpl implements HeadLineService {
    private final HeadLineDao headLineDao;
    private final JedisUtil.Keys keys;
    private final JedisUtil.Strings strings;

    public HeadLineServiceImpl(HeadLineDao headLineDao, JedisUtil.Keys keys, JedisUtil.Strings strings) {
        this.headLineDao = headLineDao;
        this.keys = keys;
        this.strings = strings;
    }

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLine) throws HeadLineOperationException {
        String key = HEAD_LINE_KEY;
        List<HeadLine> headLineList = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if (keys.exists(key)) {
            // get value from redis cache
            log.debug("read value from redis...............");
            String headLineListString = strings.getValue(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = objectMapper.readValue(headLineListString, javaType);
            } catch (JsonProcessingException e) {
                log.error("failed to read value from redis....");
                throw new HeadLineOperationException(e.getMessage());
            }
        } else {
            log.debug("read value from mysql..........");
            try {
                headLineList = headLineDao.queryHeadLineList(headLine);
                String headLineListString = objectMapper.writeValueAsString(headLineList);
                strings.setPair(key, headLineListString);
            } catch (Exception e) {
                log.error("set key value pair into redis failed......");
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        // here set there is default data about headline in database
        return headLineList;
    }
}
