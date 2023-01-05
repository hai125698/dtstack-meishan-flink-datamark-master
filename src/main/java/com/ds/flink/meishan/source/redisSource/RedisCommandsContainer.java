package com.ds.flink.meishan.source.redisSource;

import com.ds.flink.meishan.proto.Che;
import com.ds.flink.meishan.proto.Pow;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: RedisCommandsContainer
 * @Description: 接口类 定义redis的读取操作
 * @author: ds-longju
 * @Date: 2022-10-31 15:07
 * @Version 1.0
 **/
public interface RedisCommandsContainer {

    // hash 类型操作
    Map<String,String> hgetAll(String key);

    Map<String, Che> hgetAllChe(String key);

    Map<String, Pow> hgetAllPow(String key);

    String hget(String key, String field);

    List<String> hmget(String key, String... field);

    // Set 类型操作
    Set<String> smembers(String key);

    // List 类型操作
    List<String> lrange(String key ,Long start_index,Long end_index);



    void close();

}
