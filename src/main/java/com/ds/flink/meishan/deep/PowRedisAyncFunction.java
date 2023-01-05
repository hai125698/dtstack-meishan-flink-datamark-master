package com.ds.flink.meishan.deep;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.Che;
import com.ds.flink.meishan.proto.Pow;
import com.ds.flink.meishan.proto.ProtostuffSerializer;
import com.ds.flink.meishan.proto.ProtostuffSerializerPow;
import com.ds.flink.meishan.until.JedisPoolUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * @ClassName: PowRedisAyncFunction
 * @Description: 异步读取redis的数据
 * @author: ds-longju
 * @Date: 2022-11-10 15:57
 * @Version 1.0
 **/
public class PowRedisAyncFunction extends RichAsyncFunction<JSONObject, JSONObject> {
    private static JedisPool jedisPool = null ;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        jedisPool = JedisPoolUtil.getJedisPoolInstance();
    }

    @Override
    public void asyncInvoke(JSONObject input, ResultFuture<JSONObject> resultFuture) throws Exception {
        String powUnionKey = "PowName:" + input.getString("name") ;
        String CheUnionKey = "ioShortName:" + input.getString("name") ;

        byte[] powKey = powUnionKey.getBytes();
        byte[] cheKey = CheUnionKey.getBytes();

        byte[] pow = "Proto:EC.Pow".getBytes();
        byte[] che = "Proto:EC.Che".getBytes();

        ProtostuffSerializerPow protostuffSerializerPow = new ProtostuffSerializerPow();
        ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] powHget = jedis.hget(pow, powKey);
            byte[] cheHget = jedis.hget(che, cheKey);

            if (powHget != null){
                Pow powDeserialize = protostuffSerializerPow.deserialize(powHget);
                input.putAll(JSONObject.parseObject(powDeserialize.toString()));
            }else {
                System.out.println(powUnionKey  + "查不到数据");
            }
            if(cheHget != null){
                Che cheDeserialize = protostuffSerializer.deserialize(cheHget);
                input.putAll(JSONObject.parseObject(cheDeserialize.toString()));
            }else {
                System.out.println(CheUnionKey  + "查不到数据");
            }
            //先释放一次
            jedis.close();
            // 相同字段 以pow为准
            resultFuture.complete(Collections.singleton(input));


        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null) {
                JedisPoolUtil.release(jedisPool, jedis);
            }
        }

    }

}
