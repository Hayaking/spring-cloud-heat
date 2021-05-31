package com.haya.heatcollector.handle;


import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.BaiduGeoInfo;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.AlarmConfig;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.service.AlarmConfigService;
import com.haya.heatcollector.service.BaiduGeoService;
import com.haya.heatcollector.service.ComponentService;
import com.haya.heatcollector.service.MetricService;
import com.haya.heatcollector.utils.AlarmUtil;
import com.haya.heatcollector.utils.ComponentUpUtil;
import com.haya.heatcollector.utils.RedisLock;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author haya
 */
@Data
@Service
public class MetricHandle {
    @Value(value = "${collector.name}")
    private String collectorName;
    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private BaiduGeoService geoService;
    @Autowired
    private ThreadPoolExecutor taskExecutePoll;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void handle(HeatData heatData) {
        heatData.setCollectorName( collectorName );
        Double lon = heatData.getLon();
        Double lat = heatData.getLat();
        BaiduGeoInfo geoInfo = geoService.getGeoInfoByPoint( lon, lat );
        heatData.setArea( geoInfo.getArea() );
        heatData.setStreet( geoInfo.getStreet() );
        heatData.setAddress( geoInfo.getAddress() );
        String lockKey = "lock:" + heatData.getType() + ":" + lon + ":" + lat;
        String uuid = UUID.randomUUID().toString();
        AtomicReference<Component> componentAtomicReference = new AtomicReference<>();
        try {
            RedisLock.tryLock( lockKey, uuid, 5, 10 );
            componentAtomicReference.set( componentService.selectElseInsert( heatData ) );

        } catch (Exception ignored) {
            System.out.println(ignored);
            return;
        } finally {
            RedisLock.releaseLock( lockKey, uuid );
        }
        Component component = componentAtomicReference.get();
        Metric metric = registerMetric( heatData );
        String key = "component:up:set:" + component.getId();
        Boolean isAlarm = false;
        // 处理component_up指标
        if ("component_up".equals( metric.getName() )) {
            ComponentUpUtil.makeComponentUp( key, component, heatData );
        } else {
            List<AlarmConfig> configList = alarmConfigService.select( component, metric);
            if (!CollectionUtils.isEmpty( configList )) {
                for (AlarmConfig config : configList) {
                    Double bottom = config.getBottom();
                    Double top = config.getTop();
                    Double metricValue = heatData.getMetricValue();
                    if (metricValue >= top || metricValue <= bottom) {
                        isAlarm = true;
                        String isExist = redisTemplate.opsForValue().get( "alarm:exist" + config.getId() );
                        // 添加异常状态到set
                        taskExecutePoll.execute( () -> ComponentUpUtil.saveUptoZSet( key, config ) );
                        if (isExist == null) {
                            redisTemplate.opsForValue().set( "alarm:exist" + config.getId(), "" );
                            redisTemplate.expire( "alarm:exist" + config.getId(), 5, TimeUnit.MINUTES );
                            // 保存告警
                            taskExecutePoll.execute( () -> AlarmUtil.saveAlarm( config, component, metricValue, metric ) );
                            // 发送邮件
                            taskExecutePoll.execute( () -> AlarmUtil.sendMail( component, metric, metricValue, config ) );
                        }
                    }
                }
            }
        }

        heatData.setComponentName( component.getName() );
        heatData.setStreet( component.getStreet() );
        heatData.setArea( component.getArea() );
        heatData.setId( component.getId() );
        heatData.setArea( component.getArea() );
        heatData.setStreet( component.getStreet() );
        heatData.setIsAlarm( isAlarm );
        template.send( "data1", JSON.toJSONString( heatData ) );
    }


    /**
     * 注册指标
     *
     * @param heatData
     * @return
     */
    public Metric registerMetric(HeatData heatData) {
        Metric metric = new Metric() {{
            setType( heatData.getType() );
            setName( heatData.getMetricName() );
            setAliasName( heatData.getAliasName() );
            setUnit( heatData.getUnit() );
        }};
        // 指标注册
        return metricService.selectElseInsert( metric );
    }

}
