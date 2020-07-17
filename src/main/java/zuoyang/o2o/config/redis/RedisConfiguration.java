package zuoyang.o2o.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import zuoyang.o2o.cache.JedisPoolWriper;
import zuoyang.o2o.cache.JedisUtil;
import zuoyang.o2o.util.DESUtil;

@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostName;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    // # of max jedis objects that connection pool contains
    @Value("${redis.pool.maxActive}")
    private int maxActive;
    // # of free connection that connection pool hold
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    // max time to wait for one connection give back to pool
    @Value("${redis.pool.maxWait}")
    private long maxWaitMills;
    // check useful when get a connect from conneciton pool
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisPoolWriper;
    @Autowired
    private JedisUtil jedisUtil;



    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMills);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    @Bean(name="jedisWritePool")
    public JedisPoolWriper createJedisPoolWriper() {
        jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostName, port);
        return jedisPoolWriper;
    }

    @Bean(name="jedisUtil")
    public JedisUtil createJedisUtil() {
        jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    @Bean(name="jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    @Bean(name="jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }
}
