package com.ds.flink.meishan.main;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.Che;
import com.ds.flink.meishan.proto.ProtostuffSerializer;
import com.ds.flink.meishan.proto.User;
import com.ds.flink.meishan.source.redisSource.RedisContainer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * @ClassName:  redisReadData
 * @Description: redis 序列化数据读取
 * @author: ds-longju
 * @Date: 2022-11-02 10:38
 * @Version 1.0
 **/
public class redisReadData {
    public static void main(String[] args) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);//最大连接数
        config.setMaxIdle(10);//最大空闲连接数
        try (JedisPool jedisPool = new JedisPool(config, "10.168.86.5", 7379) ;Jedis jedis = jedisPool.getResource()) {
            RedisContainer redisContainer = new RedisContainer(jedisPool);
            Map<String, Che> stringStringMap = redisContainer.hgetAllChe("Proto:EC.Che");
            System.out.println(stringStringMap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Jedis jedis = null;
//        try{
//            ProtostuffSerializer serializer = new ProtostuffSerializer();
//            byte[] key = "Proto:EC.Che".getBytes();
//            byte[] fields = "ioShortName:AT541".getBytes();
//            jedis = new Jedis("10.168.86.5",7379);
//            jedis.auth("iecsPWD");
//            //获取用户对象
//            byte[] resultByte = jedis.hget(key,fields);
//            // 反序列化
//            Che cheResult = serializer.deserialize(resultByte);
//            System.out.println(cheResult.toString());
//            JSONObject json= JSONObject.parseObject(cheResult.toString());
//            System.out.println(json);
//
//        }catch (Exception e){
//            System.out.println(e);
//        }finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//
//        }

//        Jedis jedis = null;
//        try {
//            ProtostuffSerializer serializer = new ProtostuffSerializer();
//            User user = new User(1L, "Lisi", 18);
//            // 序列化用户对象
//            byte[] userByte = serializer.serialize(user);
//            byte[] key = "User:1".getBytes();
//            jedis = new Jedis("120.26.126.158", 6379);
//            jedis.auth("000415abc");
//            // 存储用户对象
//            jedis.set(key, userByte);
//            // 获取用户对象
//            byte[] resultByte = jedis.get(key);
//            // 反序列化
//            User userResult = serializer.deserialize(resultByte);
//            System.out.println(userResult.toString());
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }

    }


}
