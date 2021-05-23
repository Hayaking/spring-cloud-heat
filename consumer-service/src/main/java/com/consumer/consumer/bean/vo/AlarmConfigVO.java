package com.consumer.consumer.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import pojo.AlarmConfig;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mtime;
}
