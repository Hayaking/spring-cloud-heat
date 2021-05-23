package com.haya.heatcollector.utils;

import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.AlarmConfig;
import com.haya.heatcollector.entity.Component;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;


public class ComponentUpUtil {

    private static final RedisTemplate<String, String> redisTemplate = SpringBeanUtil.getBean( StringRedisTemplate.class );

    public static void saveUptoZSet(String key, AlarmConfig config) {
        redisTemplate.opsForZSet().add( key, config.getLevel().toString(), config.getLevel() );
        redisTemplate.expire( key, 30, TimeUnit.SECONDS );
    }


    /**
     * 往redis里设置可用性
     * @param key
     * @param component
     * @param heatData
     */
    public static void makeComponentUp(String key, Component component, HeatData heatData) {
        Set<String> upSet = redisTemplate.opsForZSet().rangeByScore( key, 0, 3 );
        double up = 0D;
        if (!CollectionUtils.isEmpty( upSet )) {
            up = upSet.stream()
                    .mapToDouble( Double::parseDouble )
                    .max()
                    .orElse( 0 );
        }
        redisTemplate.opsForZSet().removeRange( key, 0, 3 );
        heatData.setMetricValue( up );
        redisTemplate.opsForValue().set( "component:up:" + component.getId(), String.valueOf( (int) up ) );
        redisTemplate.expire( "component:up:" + component.getId(), 70, TimeUnit.SECONDS );
    }
}
