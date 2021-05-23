package com.consumer.consumer.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Pipeline;

import java.util.Date;
import java.util.List;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PipelineVo extends Pipeline {
    private List<Integer> sensorIdList;
    private Double temperature;
    private Double flow;
    private Double pressure;
    private Double level;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;


}
