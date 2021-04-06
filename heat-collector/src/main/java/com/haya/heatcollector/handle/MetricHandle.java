package com.haya.heatcollector.handle;


import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.service.ComponentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author haya
 */
@Data
@Service
public class MetricHandle {
    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ThreadPoolExecutor taskExecutePoll;

    public void handle(HeatData heatData) {
        Component component = componentService.selectElseInsert( heatData );
        heatData.setId( component.getId() );
        heatData.setArea( component.getArea() );
        heatData.setStreet( component.getStreet() );
        template.send( "data1", JSON.toJSONString( heatData ) );
    }
}
