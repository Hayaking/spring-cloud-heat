package com.haya.heatcollector.bean;

import com.alibaba.fastjson.annotation.JSONField;
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
public class HeatData implements Serializable {
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private Integer id;
    // 热站id
    private Integer stationId;
    // 传感器id
    private Integer sensorId;
    private String metricName;
    private Double metricValue;
    private String type;
    private String area;
    private String street;
    // 经度
    private Double lon;
    // 纬度
    private Double lat;

    public HeatData(Date time, Integer id, Integer stationId, Integer sensorId, String metricName, Double metricValue, String type, String area, String street, Double lon, Double lat) {
        this.time = time;
        this.id = id;
        this.stationId = stationId;
        this.sensorId = sensorId;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.type = type;
        this.area = area;
        this.street = street;
        this.lon = lon;
        this.lat = lat;
    }
}
