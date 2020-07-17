package zuoyang.o2o.service.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuoyang.o2o.cache.JedisUtil;
import zuoyang.o2o.dao.AreaDao;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.exception.AreaOperationException;
import zuoyang.o2o.service.AreaService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AreaServiceImpl implements AreaService {
    private final AreaDao areaDao;
    private final JedisUtil.Keys keys;
    private final JedisUtil.Strings Strings;

    public AreaServiceImpl(AreaDao areaDao, JedisUtil.Keys keys, JedisUtil.Strings strings) {
        this.areaDao = areaDao;
        this.keys = keys;
        Strings = strings;
    }

    @Override
    public List<Area> getAreaList() throws AreaOperationException {
        String key = AREA_LIST_KEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        if (keys.exists(key)) {
            // get value from redis cache
            String areaJsonString = Strings.getValue(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                log.debug("get area list from redis.....");
                areaList = mapper.readValue(areaJsonString, javaType);
            } catch (JsonProcessingException e) {
                log.error("get area list from redis failed " + e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        } else {
            // get arealist from database
            try {
                log.debug("get area list from mysql.....");
                areaList = areaDao.queryArea();
                String areaJsonString = mapper.writeValueAsString(areaList);
                Strings.setPair(key, areaJsonString);
            } catch (Exception e) {
                log.error("set area pair into redis failed " + e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        if (areaList != null) {
            return areaList;
        } else {
            throw new AreaOperationException("failed to get area list");
        }
    }
}
