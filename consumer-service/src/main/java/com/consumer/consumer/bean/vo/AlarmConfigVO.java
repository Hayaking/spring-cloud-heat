package com.consumer.consumer.bean.vo;

import lombok.*;
import pojo.AlarmConfig;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class AlarmConfigVO extends AlarmConfig {
    private String componentName;
    private Integer componentType;
    private String metricName;
    private String userName;
}
