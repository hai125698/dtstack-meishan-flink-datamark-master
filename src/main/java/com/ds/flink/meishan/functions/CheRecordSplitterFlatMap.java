package com.ds.flink.meishan.functions;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.source.redisSource.RedisRecord;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisDataType;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * @ClassName:
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-02 18:19
 * @Version 1.0
 **/
public class CheRecordSplitterFlatMap implements FlatMapFunction<RedisRecord, JSONObject> {
    @Override
    public void flatMap(RedisRecord redisRecord, Collector<JSONObject> out) throws Exception {
        assert redisRecord.getRedisDataType() == RedisDataType.HASH;
        Map<String, String> map = (Map<String, String>) redisRecord.getData();
        for (Map.Entry<String, String> e : map.entrySet()) {
            String value = e.getValue();
            JSONObject resultJson = JSONObject.parseObject(value);
            out.collect(resultJson);
        }

    }
}
