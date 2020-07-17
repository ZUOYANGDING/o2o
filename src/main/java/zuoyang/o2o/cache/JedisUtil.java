package zuoyang.o2o.cache;

import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.SafeEncoder;

public class JedisUtil {
    // Jedis cache life time
    private final int expire = 6000;

    public Keys KEYS;

    public Strings STRINGS;

    private JedisPool jedisPool;

    // get jedis connection pool
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    // Set jedis connection pool
    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    // get jedis object from jedis connection pool
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * keys operation class
     */
    public class Keys {
        /**
         * flush all data
         * @return
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String status = jedis.flushAll();
            jedis.close();
            return status;
        }

        /**
         * delete the <key, value> pair and return number of pairs been deleted
         * @param keys
         * @return
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * Returns all the keys matching the glob-style pattern as space separated strings.
         * @param pattern
         * @return
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }

        /**
         * check if a key exist or not
         * @param key
         * @return
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            boolean exist = jedis.exists(key);
            jedis.close();
            return exist;
        }
    }

    public class Strings {
        /**
         * get value by key
         * @param key
         * @return
         */
        public String getValue(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * set string value as the value of the key
         * if the key does not exist, create a new pair; otherwise update the value
         * @param key
         * @param value
         * @return
         */
        public String setPair(String key, String value) {
            Jedis jedis = getJedis();
            String status = jedis.set(SafeEncoder.encode(key), SafeEncoder.encode(value));
            jedis.close();
            return status;
        }

        /**
         * set string value as the value of the key
         * if the key does not exist, create a new pair; otherwise update the value
         * @param key
         * @param value
         * @return
         */
        public String setPair(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }

    }
}
