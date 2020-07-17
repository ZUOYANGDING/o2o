package zuoyang.o2o.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import zuoyang.o2o.util.DESUtil;

@Slf4j
@Getter
@Setter
public class JedisPoolWriper {
    private JedisPool jedisPool;

    /**
     * create the redis connection pool jedis
     * @param jedisPoolConfig
     * @param host
     * @param port
     */
    public JedisPoolWriper(final JedisPoolConfig jedisPoolConfig, final String host, final int port) {
        try {
            jedisPool = new JedisPool(jedisPoolConfig, host, port);
        } catch (Exception e) {
            log.error("create jedis pool ");
            e.printStackTrace();
        }
    }
}
