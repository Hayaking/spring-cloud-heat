package com.consumer.consumer.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Metric;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetricVO extends Metric {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;
}
