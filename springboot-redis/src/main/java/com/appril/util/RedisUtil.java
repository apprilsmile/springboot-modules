package com.appril.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@Component
public final class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 向Redis中存值，永久有效
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            return "0";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 根据传入Key获取指定Value
     */
    public String get(String key) {
        Jedis jedis = null;
        String value;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            return "0";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    /**
     * 校验Key值是否存在
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除指定Key-Value
     */
    public Long delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向Redis中存值，有超时时间
     */
    public String setExpire(String key, String value, long expireSecond) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            boolean keyExist = jedis.exists(key);
            if (keyExist) {
                jedis.del(key);
            }
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            SetParams params = new SetParams();
            params.ex((int) expireSecond);
            params.nx();
            return jedis.set(key, value, params);
        } catch (Exception e) {
            return "0";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}

