package com.appril.datasource.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.UUID;
@Component
@Slf4j
public class RedisLockUtil {
    private ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    @Autowired
    private RedisTemplate       redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String  UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取锁
     *
     * @param lockKey     锁对象
     * @param expire      锁默认持有时间[毫秒]
     * @param retryTimes  重试次数
     * @param sleepMillis 每次重试等待时长[毫秒]
     * @return
     */
    public boolean tryLock(String lockKey, long expire, int retryTimes, long sleepMillis) {
        boolean result = lock(lockKey, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                log.debug("lock :{} failed, retrying... {}", lockKey, retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.error("tryLock lockKey: {} error", lockKey, e);
                Thread.currentThread().interrupt();
                return false;
            }
            result = lock(lockKey, expire);
        }
        return result;
    }

    /**
     * 等待锁结束并查询有效的缓存结果
     * @param lockKey     锁对象
     * @param resultKey   并发结果key
     * @param retryTimes  重试次数
     * @param sleepMillis 每次重试等待时长[毫秒]
     * @return
     */
    public Object waitLockAndGetResult(String lockKey, String resultKey, int retryTimes,
                                       long sleepMillis) {
        while (barrierExists(lockKey) && retryTimes-- > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.error("waitLockAndGetResult lockKey: {} error", lockKey, e);
                Thread.currentThread().interrupt();
            }
        }
        return stringRedisTemplate.opsForValue().get(resultKey);
    }

    /**
     * 设置锁
     *
     * @param key
     * @param expire
     * @return
     */
    private boolean lock(final String key, final long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String reqId = UUID.randomUUID().toString();
                //绑定到线程 避免其他人释放锁
                lockFlag.set(reqId);
                return connection.set(key.getBytes(Charset.forName("UTF-8")),
                        reqId.getBytes(Charset.forName("UTF-8")),
                        Expiration.milliseconds(expire),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock :{} error.", key, e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key) {
        /**
         * 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，
         * 此时有可能已经被另外一个线程持有锁，所以不能直接删除
         */
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String value = lockFlag.get();
                return connection.eval(UNLOCK_LUA.getBytes(),
                        ReturnType.BOOLEAN,
                        1,
                        key.getBytes(Charset.forName("UTF-8")),
                        value.getBytes(Charset.forName("UTF-8")));
            };
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("release lock :{} occured an exception", key, e);
        } finally {
            // 清除掉ThreadLocal中的数据，避免内存溢出
            lockFlag.remove();
        }
        return false;
    }

    /**
     * 获取锁
     *
     * @param lockKey     锁对象
     * @param expire      锁默认持有时间[毫秒]
     * @param retryTimes  重试次数
     * @param sleepMillis 每次重试等待时长[毫秒]
     * @return
     */
    public boolean trySetBarrier(String lockKey, long expire, int retryTimes, long sleepMillis) {
        boolean result = lock(lockKey, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                log.debug("lock :{} failed, retrying... {}", lockKey, retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.error("tryLock lockKey: {} error", lockKey, e);
                Thread.currentThread().interrupt();
                return false;
            }
            result = setBarrier(lockKey, expire);
        }
        return result;
    }

    /**
     * 设置锁
     *
     * @param key
     * @param expire
     * @return
     */
    private boolean setBarrier(final String key, final long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String reqId = UUID.randomUUID().toString();
                //绑定到线程 避免其他人释放锁
                return connection.set(key.getBytes(Charset.forName("UTF-8")),
                        reqId.getBytes(Charset.forName("UTF-8")),
                        Expiration.milliseconds(expire),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock :{} error.", key, e);
        }
        return false;
    }

    public boolean releaseBarrier(String key) {
        /**
         * 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，
         * 此时有可能已经被另外一个线程持有锁，所以不能直接删除
         */
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.eval(UNLOCK_LUA.getBytes(),
                        ReturnType.BOOLEAN,
                        1,
                        key.getBytes(Charset.forName("UTF-8")),
                        key.getBytes(Charset.forName("UTF-8")));
            };
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("release lock :{} occured an exception", key, e);
        }
        return false;
    }

    /**
     * 判断锁是否存在
     * @param key
     * @return
     */
    public boolean barrierExists(final String key) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.exists(key.getBytes(Charset.forName("UTF-8")));
            };
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis read lock :{} error.", key, e);
        }
        return false;
    }

    public boolean tryReadBarrier(String lockKey, int retryTimes, long sleepMillis) {
        boolean result = barrierExists(lockKey);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                log.debug("read lock :{} failed, retrying... {}", lockKey, retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.error("tryLock lockKey: {} error", lockKey, e);
                Thread.currentThread().interrupt();
                return false;
            }
            result = barrierExists(lockKey);
        }
        return result;
    }

}
