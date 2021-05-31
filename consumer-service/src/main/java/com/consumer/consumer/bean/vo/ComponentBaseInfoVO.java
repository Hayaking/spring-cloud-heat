package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentBaseInfoVO extends Component {
    private Double temperature;
    private Double temperatureOut;
    private Double flow;
    private Double pressure;
    private Double pressureIncrease;
    private Double level;
    private Double watt;
    private Double voltage;
    private Double eQuantity;
    private Long upTime;
    private Long valve;
    private Integer state;
    private Integer up;
    private List<String> picList;
    private List<Component> sensorList;
    private String collectorName;
    private String stationName;
    private String pipelineName;
}
