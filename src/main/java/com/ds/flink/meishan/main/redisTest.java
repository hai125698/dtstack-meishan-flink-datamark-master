package com.ds.flink.meishan.main;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.Pow;
import com.ds.flink.meishan.proto.ProtostuffSerializerPow;
import com.ds.flink.meishan.until.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName: redisTest
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-10 16:59
 * @Version 1.0
 **/
public class redisTest {
    public static void main(String[] args) {
        String che = "PowName:" + "CR48";
        byte[] cheKey = che.getBytes();
        ProtostuffSerializerPow protostuffSerializerPow = new ProtostuffSerializerPow();
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] pow = "Proto:EC.Pow".getBytes();
            byte[] hget = jedis.hget(pow, cheKey);
            if (hget != null){
                Pow deserialize = protostuffSerializerPow.deserialize(hget);
                System.out.println(deserialize.toString());
                System.out.println(JSONObject.parseObject(deserialize.toString()));
            }else {
                System.out.println(che + "查不到数据");
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisPoolUtil.release(jedisPool, jedis);
        }
    }
}
