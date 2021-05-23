package com.consumer.consumer.bean.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.Component;
import pojo.Station;

import java.util.List;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StationVO extends Station {
    private List<Integer> componentIdList;
    private List<Component> componentList;
    private Boolean isStation = true;
}
