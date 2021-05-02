package com.consumer.consumer.bean;

import lombok.Data;

@Data
public class ComponentFilter {
    private String name;
    private Integer up;
    private Integer pageSize;
    private Integer pageNum;
    private Integer type;

}
