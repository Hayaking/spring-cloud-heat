package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentBaseInfoVO extends Component {
    private Double temperature;
    private Double flow;
    private Double pressure;
    private Double level;
}
