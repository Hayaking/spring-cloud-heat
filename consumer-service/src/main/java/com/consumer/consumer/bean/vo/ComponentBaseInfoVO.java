package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentBaseInfoVO extends Component {
    private Double temperature;
    private Double flow;
    private Double pressure;
    private Double level;
    private Double watt;
    private Double voltage;
    private Integer state;
    private List<String> picList;
    private List<Component> sensorList;
}
