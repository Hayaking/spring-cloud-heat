package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.mapper.MetricMapper;
import com.haya.heatcollector.service.MetricService;
import com.haya.heatcollector.utils.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author haya
 */
@Service
public class MetricServiceImpl extends ServiceImpl<MetricMapper, Metric> implements MetricService {
    @Autowired
    private MetricMapper metricMapper;
    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = "metric", key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    public Metric selectElseInsert(Metric metric) {
        String uuid = UUID.randomUUID().toString();
        String lockKey = "metric:lock:" + metric.getType() + ":" + metric.getName();
        RedisLock.tryLock( lockKey, uuid, 5, 10 );
        Metric res = null;
        try {
            QueryWrapper<Metric> wrapper = new QueryWrapper<>();
            wrapper.eq( "name", metric.getName() )
                    .eq( "type", metric.getType() );
            res = metricMapper.selectOne( wrapper );
            if (res == null) {
                metricMapper.insert( metric );
                res = metricMapper.selectOne( wrapper );
            }
        } catch (Exception e) {
            log.error( "", e );
        } finally {
            RedisLock.releaseLock( lockKey, uuid );
        }

        return res;
    }

    @CachePut(cacheNames = "metric", key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    @Override
    public Metric test(Metric metric) {
        System.out.println("~~~~~~~");
        return metric;
    }
}
