package com.security.security.bean;

import lombok.Data;

import java.util.List;

/**
 * @author haya
 */
@Data
public class ComponentFilter {
    private Integer id;
    private String name;
    private Integer up;
    private Integer pageSize;
    private Integer pageNum;
    private Integer type;
    private List<Item> data;

}

