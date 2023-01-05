package com.ds.flink.meishan.functions;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.source.redisSource.RedisRecord;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisDataType;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * @ClassName: RedisRecordSplitterFlatMap
 * @Description: 数据切分
 * @author: ds-longju
 * @Date: 2022-10-31 16:28
 * @Version 1.0
 **/
public class RedisRecordSplitterFlatMap implements FlatMapFunction<RedisRecord,JSONObject> {
    @Override
    public void flatMap(RedisRecord redisRecord, Collector<JSONObject> collector) throws Exception {
        assert redisRecord.getRedisDataType() == RedisDataType.HASH;
        Map<String, String> map = (Map<String, String>) redisRecord.getData();
        for (Map.Entry<String, String> e : map.entrySet()) {
            JSONObject redisJson = new JSONObject();
            redisJson.put("key",e.getKey());
            JSONObject valueJson = JSONObject.parseObject(e.getValue());
            redisJson.putAll(valueJson);
            collector.collect(redisJson);
        }

    }
}
