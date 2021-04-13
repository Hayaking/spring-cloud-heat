package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentVO extends Component {
    private String aliasName;
    private Double flow;
    private Double temperature;
    private Double pressure;
}
