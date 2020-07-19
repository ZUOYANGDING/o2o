package zuoyang.o2o.service.serviceImp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuoyang.o2o.cache.JedisUtil;
import zuoyang.o2o.service.CacheService;

import java.util.Set;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {
    private final JedisUtil.Keys targetKey;

    public CacheServiceImpl(JedisUtil.Keys targetKey) {
        this.targetKey = targetKey;
    }

    @Override
    public void removeCache(String keyPrefix) {
        log.debug("clean the cache with keyPrefix: " + keyPrefix);
        keyPrefix += "*";
        Set<String> set = targetKey.keys(keyPrefix);
        for (String key : set) {
            targetKey.del(key);
        }
    }
}
