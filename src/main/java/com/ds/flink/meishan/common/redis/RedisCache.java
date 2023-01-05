package com.ds.flink.meishan.common.redis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @ClassName: RedisCache
 * @Description: redis数据获取
 * @author: ds-longju
 * @Date: 2022-10-24 16:09
 * @Version 1.0
 **/
public class RedisCache  implements CacheAble{

    private JedisPool jedisPool;

    // redis模式
    private int model;

    private JedisSentinelPool jedisSentinelPool;

    private JedisCluster jedisCluster;

    public RedisCache(int model) {
        this.model = model;
    }


    @Override
    public void setCache (String key , String value ){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                resource.set(key , value);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                resource.set(key , value);
            }
        }else{
            jedisCluster.set(key , value);
        }

    }

    @Override
    public void setCache (String key , byte[] value){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                resource.set(key.getBytes() , value);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                resource.set(key.getBytes() , value);
            }
        }else{
            jedisCluster.set(key.getBytes() , value);
        }

    }

    @Override
    public void setCacheBySeconds (String key , String value , int seconds){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                resource.setex(key , seconds , value);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                resource.setex(key , seconds , value);
            }
        }else{
            jedisCluster.setex(key , seconds , value);
        }
    }

    @Override
    public String getCache (String key){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.get(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.get(key);
            }
        }else{
            return jedisCluster.get(key);
        }
    }

    @Override
    public byte[] getByteCache (String key){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.get(key.getBytes());
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.get(key.getBytes());
            }
        }else{
            return jedisCluster.get(key.getBytes());
        }
    }

    @Override
    public long delCache (final String... keys){
        if(keys == null || keys.length == 0){
            return 0;
        }

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.del(keys);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.del(keys);
            }
        }else{
            return jedisCluster.del(keys);
        }

    }

    @Override
    public void expire (String key , int seconds){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                resource.expire(key , seconds);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                resource.expire(key , seconds);
            }
        }else{
            jedisCluster.expire(key , seconds);
        }
    }

    @Override
    public boolean existsKey (String key){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.exists(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.exists(key);
            }
        }else{
            return jedisCluster.exists(key);
        }
    }

    public Set<String> getkeys(String pattern){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.keys(pattern);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.keys(pattern);
            }
        }else{
            TreeSet<String> keys = new TreeSet<>();
            Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
            for(String k : clusterNodes.keySet()){
                JedisPool jp = clusterNodes.get(k);
                Jedis connection = jp.getResource();
                try {
                    keys.addAll(connection.keys(pattern));
                }finally{
                    connection.close();//用完一定要close这个链接！！！
                }
            }
            return keys;
        }
    }

    @Override
    public Long setnx(String key, String value) {
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.setnx(key , value);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.setnx(key , value);
            }
        }else{
            return jedisCluster.setnx(key , value);
        }
    }

    @Override
    public boolean setnx(String key, String value, int seconds) {
        String l;
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                l = resource.set(key,value,"nx","ex",seconds);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                l = resource.set(key,value,"nx","ex",seconds);
            }
        }else{
            l = jedisCluster.set(key,value,"nx","ex",seconds);
        }
        return "OK".equals(l);
    }

    @Override
    public boolean setnx(String key, String value, int seconds, int wait,long interval ) throws InterruptedException {
        String l = "";
        if(interval > 1000){
            interval = 1000;
        }
        if(wait > 5){
            wait = 5;
        }
        if(seconds > 5){
            seconds = 5;
        }
        long start = System.currentTimeMillis();
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                while(!"OK".equals(l) && (System.currentTimeMillis()-start)<wait*1000) {
                    l = resource.set(key, value, "nx", "ex", seconds);
                    Thread.sleep(interval);
                }
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                while(!"OK".equals(l) && (System.currentTimeMillis()-start)<wait*1000) {
                    l = resource.set(key, value, "nx", "ex", seconds);
                    Thread.sleep(interval);
                }
            }
        }else{
            while(!"OK".equals(l) && (System.currentTimeMillis()-start)<wait*1000) {
                l = jedisCluster.set(key, value, "nx", "ex", seconds);
                Thread.sleep(interval);
            }
        }
        return "OK".equals(l);
    }


    @Override
    public void publish(String channel, String message){

        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                resource.publish(channel, message);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                resource.publish(channel, message);
            }
        }else{
            jedisCluster.publish(channel, message);
        }

    }

    @Override
    public long zrem(String key, String... members){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zrem(key,members);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zrem(key,members);
            }
        }else{
            return jedisCluster.zrem(key,members);
        }
    }

    @Override
    public long zremrangeByRank(String key, long start, long stop){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zremrangeByRank(key,start,stop);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zremrangeByRank(key,start,stop);
            }
        }else{
            return jedisCluster.zremrangeByRank(key,start,stop);
        }
    }

    /**
     * by hubert -- 2022-04-21
     * 增加一个redis方法，方便清理某些数据
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public long zremrangeByScore(String key, double min, double max) {
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zremrangeByScore(key,min,max);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zremrangeByScore(key,min,max);
            }
        }else{
            return jedisCluster.zremrangeByScore(key,min,max);
        }
    }

    @Override
    public long zadd(String key, String member , Double score){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zadd(key,score,member);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zadd(key,score,member);
            }
        }else{
            return jedisCluster.zadd(key,score,member);
        }
    }

    @Override
    public long zadd(String key, Map<String,Double> members){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zadd(key,members);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zadd(key,members);
            }
        }else{
            return jedisCluster.zadd(key,members);
        }
    }

    @Override
    public long sadd(String key, String member){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.sadd(key,member);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.sadd(key,member);
            }
        }else{
            return jedisCluster.sadd(key,member);
        }
    }

    @Override
    public long sadd(String key, Set<String> members){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.sadd(key,members.toArray(new String[]{}));
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.sadd(key,members.toArray(new String[]{}));
            }
        }else{
            return jedisCluster.sadd(key,members.toArray(new String[]{}));
        }
    }

    @Override
    public Set<String> smembers(String key){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.smembers(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.smembers(key);
            }
        }else{
            return jedisCluster.smembers(key);
        }
    }

    @Override
    public long srem(String key,String ... smembers){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.srem(key,smembers);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.srem(key,smembers);
            }
        }else{
            return jedisCluster.srem(key,smembers);
        }
    }

    @Override
    public long zcard(String key){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zcard(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zcard(key);
            }
        }else{
            return jedisCluster.zcard(key);
        }
    }

    @Override
    public long zcount(String key, Double min, Double max){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zcount(key,min,max);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zcount(key,min,max);
            }
        }else{
            return jedisCluster.zcount(key,min,max);
        }
    }

    @Override
    public Set<String> zrange(String key, long min, long max){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zrange(key,min,max);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zrange(key,min,max);
            }
        }else{
            return jedisCluster.zrange(key,min,max);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long min, long max){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zrevrange(key,min,max);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zrevrange(key,min,max);
            }
        }else{
            return jedisCluster.zrevrange(key,min,max);
        }
    }

    @Override
    public long zrank(String key, String member){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zrank(key,member);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zrank(key,member);
            }
        }else{
            return jedisCluster.zrank(key,member);
        }
    }

    @Override
    public double zincrby(String key, Double increment, String member){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zincrby(key,increment,member);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zincrby(key,increment,member);
            }
        }else{
            return jedisCluster.zincrby(key,increment,member);
        }
    }

    @Override
    public long hset(String key, String field , String value){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hset(key,field,value);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hset(key,field,value);
            }
        }else{
            return jedisCluster.hset(key,field,value);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hmset(key,hash);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hmset(key,hash);
            }
        }else{
            return jedisCluster.hmset(key,hash);
        }
    }

    @Override
    public String hget(String key, String field){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hget(key,field);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hget(key,field);
            }
        }else{
            return jedisCluster.hget(key,field);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hgetAll(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hgetAll(key);
            }
        }else{
            return jedisCluster.hgetAll(key);
        }
    }

    @Override
    public List<String> hmget(String key, String... field){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hmget(key,field);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hmget(key,field);
            }
        }else{
            return jedisCluster.hmget(key,field);
        }
    }

    @Override
    public long hlen(String key){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hlen(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hlen(key);
            }
        }else{
            return jedisCluster.hlen(key);
        }
    }

    @Override
    public Set<String> hkeys(String key){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hkeys(key);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hkeys(key);
            }
        }else{
            return jedisCluster.hkeys(key);
        }
    }

    @Override
    public boolean hmexists(String key, String... fields){
        boolean exists = true;
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                for(String field:fields){
                    exists = exists && resource.hexists(key,field);
                    if(!exists)break;
                }
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                for(String field:fields){
                    exists = exists && resource.hexists(key,field);
                    if(!exists)break;
                }
            }
        }else{
            for(String field:fields){
                exists = exists && jedisCluster.hexists(key,field);
                if(!exists)break;
            }
        }
        return exists;
    }

    @Override
    public boolean hexists(String key, String field){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hexists(key,field);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hexists(key,field);
            }
        }else{
            return jedisCluster.hexists(key,field);
        }
    }

    @Override
    public long hdel(String key, String... field){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hdel(key,field);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hdel(key,field);
            }
        }else{
            return jedisCluster.hdel(key,field);
        }
    }

    @Override
    public long incr(String key, int seconds){
        long l;
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                l = resource.incrBy(key,1);
                resource.expire(key,seconds);
                return l;
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                l = resource.incrBy(key,1);
                resource.expire(key,seconds);
                return l;
            }
        }else{
            l = jedisCluster.incrBy(key,1);
            jedisCluster.expire(key,seconds);
            return l;
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.hscan(key,cursor,params);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.hscan(key,cursor,params);
            }
        }else{
            return jedisCluster.hscan(key,cursor,params);
        }
    }

    @Override
    public String flushDB(){
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.flushDB();
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.flushDB();
            }
        }else{
            return jedisCluster.scriptFlush("".getBytes());
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end){
        long l;
        if (this.model == 1) {
            try (Jedis resource=jedisPool.getResource()) {
                return resource.zrevrangeWithScores(key,start,end);
            }
        }else if(this.model == 2){
            try (Jedis resource=jedisSentinelPool.getResource()) {
                return resource.zrevrangeWithScores(key,start,end);
            }
        }else{
            return jedisCluster.zrevrangeWithScores(key,start,end);
        }
    }
}
