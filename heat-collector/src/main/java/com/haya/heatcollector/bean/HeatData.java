package com.haya.heatcollector.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haya
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HeatData implements Serializable {
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date time;
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
    private String ip;
    private Integer port;
    private String address;

}
