package com.haya.heatcollector.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("alarm_config")
public class AlarmConfig extends Model<AlarmConfig> implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer level;
    private Integer componentId;
    private String componentName;
    private Integer componentType;
    private Integer metricId;
    private String metricName;
    private Double top;
    private Double bottom;
    private Integer userId;
    private String userName;
    private Date ctime;
    private Date mtime;
}
