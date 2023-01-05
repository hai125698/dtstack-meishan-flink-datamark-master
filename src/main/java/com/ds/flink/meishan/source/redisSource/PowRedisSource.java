package com.ds.flink.meishan.source.redisSource;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisConfigBase;
import org.apache.flink.util.Preconditions;

/**
 * @ClassName: PowRedisSource
 * @Description: redis pow 数据读取
 * @author: ds-longju
 * @Date: 2022-11-10 14:44
 * @Version 1.0
 **/
public class PowRedisSource extends RichSourceFunction<RedisRecord> {

    private static final long serialVersionUID = 1L;
    private String additionalKey;
    private RedisCommandDescription.RedisCommand redisCommand;
    private FlinkJedisConfigBase flinkJedisConfigBase;
    private RedisCommandsContainer redisCommandsContainer;
    private volatile boolean isRunning = true;

    /***
     * 创建redis连接
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        this.redisCommandsContainer = RedisCommandsContainerBuilder.build(this.flinkJedisConfigBase);
    }


    public PowRedisSource(FlinkJedisConfigBase flinkJedisConfigBase, RedisCommandDescription redisCommandDescription) {
        Preconditions.checkNotNull(flinkJedisConfigBase, "Redis connection pool config should not be null");
        Preconditions.checkNotNull(redisCommandDescription, "MyRedisCommandDescription  can not be null");
        this.flinkJedisConfigBase = flinkJedisConfigBase;
        this.redisCommand = redisCommandDescription.getCommand();
        this.additionalKey = redisCommandDescription.getAdditionalKey();
    }

    @Override
    public void run(SourceContext<RedisRecord> sourceContext) throws Exception {
        while (isRunning) {
            sourceContext.collect(new RedisRecord(this.redisCommandsContainer.hgetAllPow(this.additionalKey), this.redisCommand.getRedisDataType()));
            Thread.sleep(5000L);
        }

    }

    @Override
    public void cancel() {
        isRunning = false;
        if (this.redisCommandsContainer != null) {
            this.redisCommandsContainer.close();
        }
    }
}
