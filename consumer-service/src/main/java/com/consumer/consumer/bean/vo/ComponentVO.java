package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentVO extends Component {
    private String aliasName;
    private Double flow;
    private Double flowSpeed;
    private Double temperature;
    private Double temperatureSpeed;
    private Double temperatureOut;
    private Double pressure;
    private Double pressureSpeed;
    private Double waterLevel;
    private Double watt;
    private Double voltage;
    private Integer state;
    private Boolean isStation;
    private Integer up;

}
