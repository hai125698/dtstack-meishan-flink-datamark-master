package com.ds.flink.meishan.source.redisSource;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisDataType;
import org.apache.flink.util.Preconditions;

import java.io.Serializable;

/**
 * @ClassName: RedisCommandDescription
 * @Description: redis 操作描述类
 * @author: ds-longju
 * @Date: 2022-10-31 15:06
 * @Version 1.0
 **/
public class RedisCommandDescription implements Serializable {
    public enum RedisCommand {
        HGETALL(RedisDataType.HASH);

        private RedisDataType redisDataType;

        private RedisCommand(RedisDataType redisDataType) {
            this.redisDataType = redisDataType;
        }
        public RedisDataType getRedisDataType() {
            return this.redisDataType;
        }
    }


    private static final long serialVersionUID = 1L;
    private RedisCommand redisCommand;
    private String additionalKey;

    public RedisCommandDescription(RedisCommand redisCommand, String additionalKey) {
        Preconditions.checkNotNull(redisCommand, "Redis command type can not be null");
        this.redisCommand = redisCommand;
        this.additionalKey = additionalKey;
        if ((redisCommand.getRedisDataType() == RedisDataType.HASH || redisCommand.getRedisDataType() == RedisDataType.SORTED_SET) && additionalKey == null) {
            throw new IllegalArgumentException("Hash and Sorted Set should have additional key");
        }
    }

    public RedisCommandDescription(RedisCommand redisCommand) {
        this(redisCommand, (String)null);
    }

    public RedisCommand getCommand() {
        return this.redisCommand;
    }

    public String getAdditionalKey() {
        return this.additionalKey;
    }
}
