package com.haya.heatcollector.handle;


import com.haya.heatcollector.bean.HeatData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MetricHandle {
    @Autowired
    private KafkaTemplate<String, String> template;

    public void handle(List<HeatData> heatData) {

    }
}
