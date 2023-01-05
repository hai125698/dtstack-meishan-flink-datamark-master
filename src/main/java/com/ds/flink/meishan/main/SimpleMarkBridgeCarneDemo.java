package com.ds.flink.meishan.main;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.deep.PowRedisAyncFunction;
import com.ds.flink.meishan.functions.MarkBridgeCraneMap;
import com.ds.flink.meishan.functions.WholdLoadBaseBridgeMap;
import com.ds.flink.meishan.metrics.InputMapperWithMetrics;
import com.ds.flink.meishan.metrics.OutputMapperWithMetrics;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SimpleMarkBridgeCarneDemo
 * @Description: 简单桥吊数据打标Demo
 * @author: ds-longju
 * @Date: 2022-10-31 16:47
 * @Version 1.0
 **/
public class SimpleMarkBridgeCarneDemo {
    private static final String SOURCEKAFKA = "gps_blctms_sts_deviceinfo";

    private static final String SINKKAFKA = "dwd_blctms_marking_bridgeCarne_info";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 生产环境中并行度设置为3,开发环境设置为1
        env.setParallelism(3);
        env.enableCheckpointing(10000L);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(1000000L) ;
        env.getCheckpointConfig().setPreferCheckpointForRecovery(true) ;
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 5000));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 配置redis的连接方式
//        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("10.168.86.5").setPort(7379).setPassword("x").build();

        //打标流程
        KafkSourceMarketProcessing(env);

        env.execute("FLINK CONSUMPTION MARKING DEMO ") ;

    }

    /***
     * 由于数栈不支持flink-connect-redis的包，所以注释掉
     * 桥吊基础数据写入到mysql
     * @param
     * @param env
     */
    public static void sinkBaseBridgeToMysql(StreamExecutionEnvironment env){
//        // 创建桥吊基础数据redis数据源
//        DataStreamSource<RedisRecord> sourceBaseBridgeCrane = env.addSource(new RedisSource(conf, new RedisCommandDescription(RedisCommandDescription.RedisCommand.HGETALL, "Base:BridgeCrane")));
//
//        SingleOutputStreamOperator<JSONObject> outputStreamOperator = sourceBaseBridgeCrane.flatMap(new RedisRecordSplitterFlatMap());
//
//        outputStreamOperator.print("data");
//        // 桥吊基础数据写入mysql
//        outputStreamOperator.addSink(new sinkBridgeCraneToMysql());

    }


    /***
     *由于数栈不支持flink-connect-redis的包，所以注释掉
     * redis 数据作为source,简单打标数据输出到redis
     * @param env
     */
    public static void RedisBaseBridgeCarnePrcessing(StreamExecutionEnvironment env){

//        // 反序列化读取Che的数据
//        DataStreamSource<RedisRecord> redisSourceDataStream = env.addSource(new CheRedisSource(conf, new RedisCommandDescription(RedisCommandDescription.RedisCommand.HGETALL, "Proto:EC.Che")));
//
//        //反序列化Pow的数据
//
//        SingleOutputStreamOperator<JSONObject> bridgeCraneMap = redisSourceDataStream
//                .flatMap(new LastDataStateFlatMap()).uid("FlatMap")
//                .map(new WholdLoadBaseBridgeMap())
//                .map(new MarkBridgeCraneMap());
//
//        bridgeCraneMap.print("SimpleMarkBridgeCrane");
//
//        PowRedisAyncFunction powRedisAyncFunction = new PowRedisAyncFunction();
//
//        DataStream<JSONObject> result;
//
//        if (true) {
//            result = AsyncDataStream.orderedWait(
//                    bridgeCraneMap,
//                    powRedisAyncFunction,
//                    1000000L,
//                    TimeUnit.MILLISECONDS,
//                    20);
//        } else {
//            result = AsyncDataStream.unorderedWait(
//                    bridgeCraneMap,
//                    powRedisAyncFunction,
//                    10000,
//                    TimeUnit.MILLISECONDS,
//                    20);
//        }
//
//        result.addSink(new RedisSink<JSONObject>(conf,new sinkRedisMapper()));

    }

    /***
     *  数据源改成 Kafka 异步关联redis的数据，全量关联mysql的维表数据，最后输出到kafka
     * @param env
     */

    public static void KafkSourceMarketProcessing(StreamExecutionEnvironment env){

        //添加kafka数据源配置
        Properties inKafkaPro = new Properties();
        inKafkaPro.put("bootstrap.servers","10.168.86.71:9092,10.168.86.72:9092,10.168.86.73:9092");
        inKafkaPro.put("auto.offset.reset","latest");
        inKafkaPro.put("group.id", "meiShan");

        // kafka 设备位置信息
        DataStreamSource<String> stsSource = env.addSource(new FlinkKafkaConsumer<>(SOURCEKAFKA, new SimpleStringSchema(), inKafkaPro));


        SingleOutputStreamOperator<JSONObject> stsMap = stsSource
                // inputMtrics
                .map(new InputMapperWithMetrics())
                // 过滤需要的字段
                .map(new MapFunction<String, JSONObject>() {
                    @Override
                    public JSONObject map(String s) throws Exception {
                        Date date = new Date();
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        JSONObject result = new JSONObject();
                        JSONObject source = JSONObject.parseObject(s);
                        String name = "CR" + source.getString("name"); //转换为标准格式
                        Integer trolleySpd = source.getInteger("trolleySpd");
                        Boolean spreaderUnlock = source.getBoolean("spreaderUnlock");
                        String collectTime = source.getString("collectTime");
                        result.put("name", name);
                        result.put("trolleySpd", trolleySpd);
                        result.put("spreaderUnlock", spreaderUnlock);
                        result.put("collectTime",collectTime);
                        result.put("dataStartTime", dateFormat.format(date));
                        return result;
                    }
                });

        PowRedisAyncFunction powRedisAyncFunction = new PowRedisAyncFunction();

        DataStream<JSONObject> result;

        if (true) {
            result = AsyncDataStream.orderedWait(
                    stsMap,
                    powRedisAyncFunction,
                    1000000L,
                    TimeUnit.MILLISECONDS,
                    20);
        } else {
            result = AsyncDataStream.unorderedWait(
                    stsMap,
                    powRedisAyncFunction,
                    10000,
                    TimeUnit.MILLISECONDS,
                    20);
        }

        SingleOutputStreamOperator<JSONObject> markData= result
                // 关联桥吊基础信息
                .map(new WholdLoadBaseBridgeMap())
                //打标
                .map(new MarkBridgeCraneMap())
                //outputMetrics
                .map(new OutputMapperWithMetrics())
                ;


        markData.print("markData");

        // 打标接口输出到kafka
        FlinkKafkaProducer<String> sinkKafkaProducer = new FlinkKafkaProducer<String>("10.168.86.71:9092,10.168.86.72:9092,10.168.86.73:9092",SINKKAFKA , new SimpleStringSchema());

        markData.map(new MapFunction<JSONObject, String>() {
            @Override
            public String map(JSONObject jsonObject) throws Exception {
                return jsonObject.toString();
            }
        }).addSink(sinkKafkaProducer);


    }
}
