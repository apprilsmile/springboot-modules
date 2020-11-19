package com.appril.util;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

/**
 * @description: memcached工具类
 **/
@Component
public class MemcachedUtil {

    @Autowired
    private MemcachedClient memcachedClient;

    /**
     *  保存（key值重覆则覆盖）
     * @param key   键值
     * @param value 数据
     * @param time  保存时间    如果长期则为0
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public boolean set(String key, Object value, int time) throws InterruptedException, MemcachedException, TimeoutException {
        if (memcachedClient.set(key, time, value))
            return true;
        return false;
    }

    /**
     *  获取
     * @param key   键值
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public Object get(String key) throws InterruptedException, MemcachedException, TimeoutException {
        return memcachedClient.get(key);
    }

    /**
     *  删除
     * @param key   键值
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public boolean delete(String key) throws InterruptedException, MemcachedException, TimeoutException {
        if (memcachedClient.delete(key))
            return true;
        return false;
    }

    /**
     *  替换（以key值为查找基准替换缓存）
     * @param key   键值
     * @param value 数据
     * @param time  保存时间    如果长期则为0
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public boolean replace(String key, Object value, int time) throws InterruptedException, MemcachedException, TimeoutException {
        if (memcachedClient.replace(key, time, value))
            return true;
        return false;
    }

    /**
     *  末尾追加
     * @param key   键值
     * @param value 追加数据
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public boolean append(String key, Object value) throws InterruptedException, MemcachedException, TimeoutException {
        if (memcachedClient.append(key, value))
            return true;
        return false;
    }

    /**
     *  头部追加
     * @param key   键值
     * @param value 追加数据
     * @return
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public boolean prepend(String key, Object value) throws InterruptedException, MemcachedException, TimeoutException {
        if (memcachedClient.prepend(key, value))
            return true;
        return false;
    }



}
