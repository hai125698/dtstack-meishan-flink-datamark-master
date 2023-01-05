package com.ds.flink.meishan.source.redisSource;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisDataType;

import java.io.Serializable;

/**
 * @ClassName: RedisRecord
 * @Description: 抽象redis数据:封装redis数据类型和数据对象
 * @author: ds-longju
 * @Date: 2022-10-01 16:22
 * @Version 1.0
 **/
public class RedisRecord  implements Serializable  {
    private Object data;
    private RedisDataType redisDataType;

    public RedisRecord(Object data, RedisDataType redisDataType) {
        this.data = data;
        this.redisDataType = redisDataType;
    }

    public Object getData() {
        return data;
    }

    public RedisDataType getRedisDataType() {
        return redisDataType;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setRedisDataType(RedisDataType redisDataType) {
        this.redisDataType = redisDataType;
    }

    @Override
    public String toString() {
        return "MyRedisRecord{" +
                "data=" + data +
                ", redisDataType=" + redisDataType +
                '}';
    }

}
