package com.ds.flink.meishan.source.redisSource;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisConfigBase;
import org.apache.flink.util.Preconditions;

/**
 * @ClassName: RedisSource
 * @Description: 自定义redis source
 * @author: ds-longju
 * @Date: 2022-10-31 16:25
 * @Version 1.0
 **/
public class RedisSource  extends RichSourceFunction<RedisRecord> {

    private static final long serialVersionUID = 1L;
    private String additionalKey;
    private RedisCommandDescription.RedisCommand redisCommand;
    private FlinkJedisConfigBase flinkJedisConfigBase;
    private RedisCommandsContainer redisCommandsContainer;
    private volatile boolean isRunning = true;

    public RedisSource(FlinkJedisConfigBase flinkJedisConfigBase, RedisCommandDescription redisCommandDescription) {
        Preconditions.checkNotNull(flinkJedisConfigBase, "Redis connection pool config should not be null");
        Preconditions.checkNotNull(redisCommandDescription, "MyRedisCommandDescription  can not be null");
        this.flinkJedisConfigBase = flinkJedisConfigBase;
        this.redisCommand = redisCommandDescription.getCommand();
        this.additionalKey = redisCommandDescription.getAdditionalKey();
    }


    /***
     * 创建redis连接
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        this.redisCommandsContainer = RedisCommandsContainerBuilder.build(this.flinkJedisConfigBase);
    }


    /***
     * run方法会一直读取redis数据，并根据数据类型调用对应的redis操作，封装成RedisRecord对象，够后续处理
     * @param sourceContext
     * @throws Exception
     */
    @Override
    public void run(SourceContext<RedisRecord> sourceContext) throws Exception {
        while (isRunning) {
            switch (this.redisCommand) {
                case HGETALL:
                    sourceContext.collect(new RedisRecord(this.redisCommandsContainer.hgetAll(this.additionalKey), this.redisCommand.getRedisDataType()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot process such data type: " + this.redisCommand);
            }
            Thread.sleep(86400L);
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
