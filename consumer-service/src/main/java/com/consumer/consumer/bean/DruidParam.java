package com.consumer.consumer.bean;

import lombok.Data;

@Data
public class DruidParam {
    private int id;
    private String metricName;

    private long startTimeStamp;
    private long endTimeStamp;
    private long range;

    public long getRange() {
        return endTimeStamp - startTimeStamp;
    }
}
