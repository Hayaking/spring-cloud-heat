package com.haya.heatcollector.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("component")
public class Component extends Model<Component> implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Double lon;
    private Double lat;
    private Integer stationId;
    private Integer sensorId;
    private Integer type;
    private Integer up;
    private String area;
    private String street;

    public String getCacheKey() {
        return lon + ":" + lat;
    }

}
