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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

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
        Component componentPrototype = null;
        try {
            RedisLock.tryLock( lockKey, uuid, 5, 10 );
            componentPrototype = componentService.selectElseInsert( heatData );
        } catch (Exception ignored) {
            System.out.println(ignored);
            return;
        } finally {
            RedisLock.releaseLock( lockKey, uuid );
        }
        Component component = componentPrototype;
        String key = "component:up:set:" + componentPrototype.getId();
        Metric metric = registerMetric( heatData );

        // 处理component_up指标
        if ("component_up".equals( metric.getName() )) {
            taskExecutePoll.execute( () -> ComponentUpUtil.makeComponentUp( key, component, heatData ) );
        } else {
            List<AlarmConfig> configList = alarmConfigService.select( component, metric );
            if (!CollectionUtils.isEmpty( configList )) {
                for (AlarmConfig config : configList) {
                    Double bottom = config.getBottom();
                    Double top = config.getTop();
                    Double metricValue = heatData.getMetricValue();
                    if (metricValue >= top || metricValue <= bottom) {
                        // 保存告警
                        taskExecutePoll.execute( () -> AlarmUtil.saveAlarm( config, component, metricValue, metric ) );
                        // 添加异常状态到set
                        taskExecutePoll.execute( () -> ComponentUpUtil.saveUptoZSet( key, config ) );
                        // 发送邮件
                        taskExecutePoll.execute( () -> AlarmUtil.sendMail( component, metric, metricValue, config ) );
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
        }};
        // 指标注册
        return metricService.selectElseInsert( metric );
    }

}
