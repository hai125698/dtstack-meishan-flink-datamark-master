package com.ds.flink.meishan.source.redisSource;

import com.ds.flink.meishan.proto.Che;
import com.ds.flink.meishan.proto.Pow;
import com.ds.flink.meishan.proto.ProtostuffSerializer;
import com.ds.flink.meishan.proto.ProtostuffSerializerPow;
import org.apache.flink.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: RedisContainer
 * @Description: 定义一个实现类，实现对redis的读取操作
 * @author: ds-longju
 * @Date: 2022-10-31 15:40
 * @Version 1.0
 **/
public class RedisContainer  implements RedisCommandsContainer, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(RedisContainer.class);

    private final JedisPool jedisPool;
    private final JedisSentinelPool jedisSentinelPool;


    public RedisContainer(JedisPool jedisPool) {
        Preconditions.checkNotNull(jedisPool, "Jedis Pool can not be null");
        this.jedisPool = jedisPool;
        this.jedisSentinelPool = null;
    }
    public RedisContainer(JedisSentinelPool sentinelPool) {
        Preconditions.checkNotNull(sentinelPool, "Jedis Sentinel Pool can not be null");
        this.jedisPool = null;
        this.jedisSentinelPool = sentinelPool;
    }



    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = this.getInstance();
            jedis.select(5);
            Map<String,String> map = new HashMap<String,String>();
            Set<String> fieldSet = jedis.hkeys(key);
            for(String s : fieldSet){
                map.put(s,jedis.hget(key,s));
            }
            return  map;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot get Redis message with command HGET to key {} error message {}", new Object[]{key, e.getMessage()});
            }
            throw e;
        } finally {
            this.releaseInstance(jedis);
        }
    }


    /***
     * @param key Proto:EC.Che
     * @return
     */
    @Override
    public Map<String, Che> hgetAllChe(String key) {
        Jedis jedis = null;
        try {
            ProtostuffSerializer serializer = new ProtostuffSerializer();
            jedis = this.getInstance();
            jedis.select(0);
            byte[] cheKey = key.getBytes();

            Map<String, Che> map = new HashMap<String,Che>();

            Set<byte[]> fieldSet = jedis.hkeys(cheKey);
            for(byte[] s : fieldSet){
                String skey = new String(s);
                if(skey.startsWith("ioShortName:CR")){
                    map.put(skey,serializer.deserialize(jedis.hget(cheKey,s)));
                }
            }
            return  map;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot get Redis message with command HGET to key {} error message {}", new Object[]{key, e.getMessage()});
            }
            throw e;
        } finally {
            this.releaseInstance(jedis);
        }
    }

    @Override
    public Map<String, Pow> hgetAllPow(String key) {
        Jedis jedis = null;
        try {
            ProtostuffSerializerPow protostuffSerializerPow = new ProtostuffSerializerPow();
            jedis = this.getInstance();
            jedis.select(0);
            byte[] powKey = key.getBytes();

            Map<String, Pow> map = new HashMap<String,Pow>();

            Set<byte[]> fieldSet = jedis.hkeys(powKey);
            for(byte[] s : fieldSet){
                String skey = new String(s);
                if(skey.startsWith("PowName:CR")){
                    map.put(skey,protostuffSerializerPow.deserialize(jedis.hget(powKey,s)));
                }
            }
            return  map;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot get Redis message with command HGET to key {} error message {}", new Object[]{key, e.getMessage()});
            }
            throw e;
        } finally {
            this.releaseInstance(jedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.getInstance();
            jedis.select(5);
            return jedis.hget(key,field);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                System.out.println(e);
                LOG.error("Cannot get Redis message with command HGET to key {} error message {}", new Object[]{key, e.getMessage()});
            }
            throw e;
        } finally {
            this.releaseInstance(jedis);
        }

    }

    @Override
    public List<String> hmget(String key, String... field) {
        return null;
    }

    @Override
    public Set<String> smembers(String key) {
        return null;
    }

    @Override
    public List<String> lrange(String key, Long start_index, Long end_index) {
        return null;
    }

    @Override
    public void close() {
        if (this.jedisPool != null) {
            this.jedisPool.close();
        }
        if (this.jedisSentinelPool != null) {
            this.jedisSentinelPool.close();
        }

    }
    private Jedis getInstance() {
        return this.jedisSentinelPool != null ? this.jedisSentinelPool.getResource() : this.jedisPool.getResource();
    }
    private void releaseInstance(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception var3) {
                LOG.error("Failed to close (return) instance to pool", var3);
            }

        }
    }
}
