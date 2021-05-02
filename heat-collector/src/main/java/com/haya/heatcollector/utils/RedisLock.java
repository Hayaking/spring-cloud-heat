package com.haya.heatcollector.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * https://www.cnblogs.com/moxiaotao/p/10829799.html
 */
@Slf4j
@Component
public class RedisLock {

    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 加锁 支持重试
     *
     * @param lockKey    key
     * @param requestId  uuid
     * @param expireTime 过期时间 单位秒
     * @param retryTime  重试时间 单位秒
     * @return boolean
     */
    public static boolean tryLock(String lockKey, String requestId, int expireTime, int retryTime) {
        try {
            SetParams setParams = SetParams.setParams();
            setParams.nx();
            setParams.ex( expireTime );

            RedisTemplate redisTemplate = SpringBeanUtil.getBean( StringRedisTemplate.class );
            long end = System.currentTimeMillis() + retryTime * 1000;
            while (System.currentTimeMillis() <= end) {
                //成功'OK' 失败返回空
                String result = (String) redisTemplate.execute( (RedisConnection connection) -> {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    return commands.set( lockKey, requestId, setParams );
                } );
                if (StringUtils.isNotEmpty( result )) {
                    return true;
                }

                if (retryTime > 0) {
                    TimeUnit.MILLISECONDS.sleep( 100 );
                }
            }
        } catch (Exception e) {
            log.error( "tryLock error", e );
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId
     * @return boolean
     */
    public static boolean releaseLock(String lockKey, String requestId) {
        try {
            RedisTemplate redisTemplate = SpringBeanUtil.getBean( StringRedisTemplate.class );
            Long result = (Long) redisTemplate.execute( (RedisConnection connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) { //支持集群
                    return ((JedisCluster) nativeConnection).eval( UNLOCK_LUA, Collections.singletonList( lockKey ), Collections.singletonList( requestId ) );
                } else if (nativeConnection instanceof Jedis) {
                    return ((Jedis) nativeConnection).eval( UNLOCK_LUA, Collections.singletonList( lockKey ), Collections.singletonList( requestId ) );
                }
                return 0;
            } );
            return result != null && result > 0;
        } catch (Exception e) {
            log.error( "releaseLock error,", e );
        }
        return false;
    }
}
