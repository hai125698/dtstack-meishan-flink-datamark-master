package com.ds.flink.meishan.stateManage;


import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.Che;
import com.ds.flink.meishan.source.redisSource.RedisContainer;
import com.ds.flink.meishan.source.redisSource.RedisRecord;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisDataType;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: lastDataStateFlatMap
 * @Description: 跟上次数据对比，取变更数据
 * @author: ds-longju
 * @Date: 2022-11-07 16:57
 * @Version 1.0
 **/
public class LastDataStateFlatMap extends RichFlatMapFunction<RedisRecord, JSONObject> {

    // mapState 不会很大，但是任务重启会清空
    private Map<String, Che> mapState;


    private static final Logger LOG = LoggerFactory.getLogger(RedisContainer.class);

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        mapState = new HashMap<>();
    }

    @Override
    public void flatMap(RedisRecord redisRecord, Collector<JSONObject> collector) throws Exception {
        assert redisRecord.getRedisDataType() == RedisDataType.HASH;
        Map<String, Che> map = (Map<String, Che>) redisRecord.getData();
        for (Map.Entry<String, Che> e : map.entrySet()) {
            String key = e.getKey();
            Che value = e.getValue();

            if(mapState.get(key) != null && value.toString().equals(mapState.get(key).toString())){
                System.out.println( key + ",对应的value已存在," + value.toString());
                LOG.info(key,"：对应的value已存在",value.toString());
                continue;
            }
            JSONObject resultJson = JSONObject.parseObject(value.toString());
            collector.collect(resultJson);
            mapState.put(key,value);

        }

    }
}
