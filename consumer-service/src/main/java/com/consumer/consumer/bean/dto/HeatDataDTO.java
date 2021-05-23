package com.consumer.consumer.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haya
 */
@Data
@Builder
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class HeatDataDTO implements Serializable {
    private Date __time;
    private Integer id;
    // 热站id
    private Integer stationId;
    // 传感器id
    private String sensorId;
    private String collectorName;
    private String metricName;
    private Double metricValue;
    private Integer type;
    private Integer childType;
    private String area;
    private String street;
    // 经度
    private Double lon;
    // 纬度
    private Double lat;
    private String componentName;

}
