package com.consumer.consumer.bean;

import lombok.Data;

@Data
public class DruidParam {
    private int id;
    private Integer metricId;
    private String metricName;

    private long startTimeStamp;
    private long endTimeStamp;
    private long range;
    private int type;

    public long getRange() {
        return endTimeStamp - startTimeStamp;
    }
}
