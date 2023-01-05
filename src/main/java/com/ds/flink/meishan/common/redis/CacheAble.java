package com.ds.flink.meishan.common.redis;


import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheAble {
    /**
     *  设置缓存信息
     * @param key       ：key值
     * @param value     ：value值
     */
    void setCache(String key, String value) ;

    /**
     *  设置缓存信息
     * @param key       ：key值
     * @param value     ：value值
     */
    void setCache(String key, byte[] value) ;

    /**
     * 设置缓存并设置有效时间，单位秒
     * @param key       ：key值
     * @param value     ：value值
     * @param seconds   ：有效时间，单位秒
     */
    void setCacheBySeconds(String key, String value, int seconds) ;

    /**
     * 获取缓存数据
     * @param key       ：key值
     * @return  String   ：返回 value值
     */
    String getCache(String key) ;

    /**
     * 获取缓存数据
     * @param key       ：key值
     * @return  String   ：返回 value值
     */
    byte[] getByteCache(String key) ;

    /**
     * 根据key值删除缓存
     * @param keys
     * @return  删除成功个数
     * @
     */
    long delCache(String... keys);

    /**
     * 设置key的有效时间
     * @param key
     * @param seconds       单位秒
     * @
     */
    void expire(String key, int seconds);

    boolean existsKey(String key) ;

    Set<String> getkeys(String pattern) ;

    /**
     * redis 设置 如果存在key键则设置不成功   set not eXists
     * @param key
     * @param value
     * @return 0 设置不成功 1设置成功
     */
    public Long setnx(String key, String value);

    /**
     * redis 设置 如果存在key键则设置不成功   set not eXists
     * @param key
     * @param value
     * @return false设置不成功 true设置成功
     */
    boolean setnx(String key, String value, int seconds);

    /**
     * redis 设置 如果wait时间内一直 存在key键则设置不成功   set not eXists
     * @param key
     * @param value
     * @param seconds key超时时间
     * @param wait key设置等待时间
     * @param interval 循环设置间隔时间
     * @return false设置不成功 true设置成功
     */
    boolean setnx(String key, String value, int seconds, int wait, long interval) throws InterruptedException;

    void publish(String channel, String message) ;

    /**
     *往有序集合添加单个成员
     */
    long zadd(String key, String member , Double score) ;

    /**
     *往有序集合添加多个成员
     */
    long zadd(String key, Map<String,Double> members) ;

    /**
     * 往无须集合添加单个成员
     */
    long sadd(String key, String member);

    /**
     * 往无序集合添加单个成员
     */
    long sadd(String key, Set<String> members) ;

    /**
     * 获取集合
     */
    Set<String> smembers(String key);

    /**
     * 删除集合
     */
    long srem(String key,String ... smembers);

    /**
     *获取有序集合的成员数
     */
    long zcard(String key) ;

    /**
     *删除有序集合中指定成员
     */
    long zrem(String key, String... members) ;

    /**
     *删除有序集合中指定排名区间成员
     */
    long zremrangeByRank(String key, long start, long stop) ;

    /**
     * by hubert --20220421
     *删除有序集合中指定分数区间成员
     */
    long zremrangeByScore(String key, double min, double max) ;

    /**
     *计算在有序集合中指定区间分数的成员数
     */
    long zcount(String key, Double min, Double max) ;

    //通过索引区间返回有序集合指定区间内的成员
    Set<String> zrange(String key, long min, long max) ;

    //通过倒序索引区间返回有序集合指定区间内的成员
    Set<String> zrevrange(String key, long min, long max) ;

    //返回有序集合中指定成员的索引
    long zrank(String key, String member) ;

    //有序集合中对指定成员的分数加上增量 increment
    double zincrby(String key, Double increment, String member) ;

    long hset(String key, String field, String value);

    Set<String> hkeys(String key);

    String hmset(String key, Map<String, String> hash);

    String hget(String key, String field);

    Map<String, String> hgetAll(String key);

    List<String> hmget(String key, String... field);

    long hlen(String key);

    boolean hmexists(String key, String... field);

    boolean hexists(String key, String field);

    long hdel(String key, String... field);

    long incr(String key, int seconds) ;

    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) ;

    String flushDB() ;

    Set<Tuple> zrevrangeWithScores(String key, long start, long end);

}