package com.ds.flink.meishan.sink;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.BridgeCrane;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @ClassName: sinkRedisMapper
 * @Description: 数据输出到reids
 * @author: ds-longju
 * @Date: 2022-11-08 16:29
 * @Version 1.0
 **/
public class sinkRedisMapper implements RedisMapper<JSONObject> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.HSET, "MarkResult:BridgeCrane");
    }

    @Override
    public String getKeyFromData(JSONObject jsonObject) {
        return jsonObject.getString("craneName");
    }

    @Override
    public String getValueFromData(JSONObject jsonObject) {
        return jsonObject.toString();
    }
}
