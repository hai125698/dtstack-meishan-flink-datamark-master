package com.ds.flink.meishan.until;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName: JedisPoolUtil
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-10 16:27
 * @Version 1.0
 **/
public class JedisPoolUtil {
    private static  JedisPool jedisPool = null;

    private JedisPoolUtil() {
    }

    public static JedisPool getJedisPoolInstance() {
        if (null == jedisPool) {
            synchronized (JedisPoolUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(1000);
                    poolConfig.setMaxIdle(100);
                    poolConfig.setMaxWaitMillis(100 * 1000);
                    poolConfig.setTestOnBorrow(true);

                    jedisPool = new JedisPool(poolConfig, "10.168.86.5", 7379,30000,"iecsPWD");
                }
            }
        }
        return jedisPool;
    }

    public static void release(JedisPool jedisPool, Jedis jedis) {
        if (null != jedis) {
            Jedis jedis2 = null;
            try {
                jedis2 = jedisPool.getResource();
            } finally {
                jedis2.close();
            }
        }
    }
}
