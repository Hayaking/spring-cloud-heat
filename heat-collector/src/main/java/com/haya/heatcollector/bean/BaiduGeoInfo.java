package com.haya.heatcollector.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author haya
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaiduGeoInfo implements Serializable {
    private String city;
    private String area;
    private String street;
    private String streetNumber;
    private String address;
}
