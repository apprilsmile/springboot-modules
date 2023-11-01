package com.appril.datasource.utils.redis;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 执行set操作
     *
     * @param key
     */
    public void set(final String key, final String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 执行get操作
     *
     * @param key
     * @return String
     */
    public String get(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 执行delete操作
     *
     * @param key
     * @return boolean
     */
    public boolean del(final String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 执行delete操作
     *
     * @param keys
     * @return Long
     */
    public Long del(final List<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    /**
     * 异步双删
     *
     * @param key
     * @return boolean
     */
    public boolean asynDoubleDel(final String key) {
        boolean flag = del(key);

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(2000);
                del(key);
            }
        }).start();

        return flag;
    }

    /**
     * 异步双删
     *
     * @param keys
     * @return boolean
     */
    public Long asynDoubleDel(final List<String> keys) {
        Long flag = del(keys);

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(2000);
                del(keys);
            }
        }).start();

        return flag;
    }

    /**
     * 执行set操作并且设置生存时间，单位为：秒
     *
     * @param key
     * @param value //TimeUnit.SECONDS 秒
     *              //TimeUnit.MINUTES 分
     */
    public void set(final String key, final String value, final Integer seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 执行setnx操作并且设置生存时间，单位为：秒
     *
     * @param key
     * @param value //TimeUnit.SECONDS 秒
     *              //TimeUnit.MINUTES 分
     * @return 是否加锁成功
     */
    public Boolean setNx(final String key, final String value, final Integer seconds) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 检测key是否存在
     *
     * @param key
     * @return boolean
     */
    public boolean hasKey(final String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 执行hset操作
     *
     * @param key
     * @param mapkey
     * @param mapvalue
     */
    public void hset(final String key, final String mapkey, final String mapvalue) {
        stringRedisTemplate.opsForHash().put(key, mapkey, mapvalue);
    }

    /**
     * 执行hgetAll操作
     *
     * @param key
     * @return Map<String, String>
     */
    public Map<String, String> hgetAll(final String key) {
        return (Map) stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 执行hdel操作
     *
     * @param key
     * @param strings
     * @return long
     */
    public long hdel(final String key, final String[] strings) {
        return stringRedisTemplate.opsForHash().delete(key, strings);
    }

    /**
     * 执行hkeys操作
     *
     * @param key
     * @return Set<String>
     */
    public Set<String> hkeys(final String key) {
        return (Set) stringRedisTemplate.opsForHash().keys(key);
    }

    /**
     * 执行hvalues操作
     *
     * @param key
     * @return List<String>
     */
    public List<String> hvalues(final String key) {
        return (List) stringRedisTemplate.opsForHash().values(key);
    }

    /**
     * 执行hget操作
     *
     * @param key
     * @return String
     */
    public String hget(final String key, final String mapkey) {
        return (String) stringRedisTemplate.opsForHash().get(key, mapkey);
    }

    /**
     * 执行hmset操作
     *
     * @param key
     */
    public void hmset(final String key, final Map<String, String> mapvalue) {
        stringRedisTemplate.opsForHash().putAll(key, mapvalue);
    }

    /**
     * 执行lpush操作
     *
     * @param key
     * @param value
     * @return long
     */
    public long lpush(final String key, final String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 执行lpop操作
     *
     * @param key
     * @return String
     */
    public String lpop(final String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 执行rpop操作
     *
     * @param key
     * @return String
     */
    public String rpop(final String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 执行list操作
     * 在列表中的尾部添加一个个值，返回列表的长度
     *
     * @param key
     * @return Long
     */
    public Long rpush(final String key, final String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 执行list操作
     * 在列表中的尾部添加多个值，返回列表的长度
     *
     * @param key
     * @return Long
     */
    public Long rpush(final String key, final String[] value) {
        return stringRedisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 执行list操作
     * 获取List列表
     *
     * @param key
     * @return List<String>
     */
    public List<String> lrange(final String key, final long start, final long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 执行list操作
     * 通过索引获取列表中的元素
     *
     * @param key
     * @return String
     */
    public String lindex(final String key, final long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    /**
     * 执行list操作
     * 获取列表长度，key为空时返回0
     *
     * @param key
     * @return Long
     */
    public Long llen(final String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * key锁有效时间
     *
     * @param key
     * @param seconds
     * @return boolean
     */
    public boolean expire(final String key, final Integer seconds) {
        return stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

}
