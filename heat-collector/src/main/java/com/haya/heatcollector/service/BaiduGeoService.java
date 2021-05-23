package com.haya.heatcollector.service;

import com.haya.heatcollector.bean.BaiduGeoInfo;

public interface BaiduGeoService {

    BaiduGeoInfo getGeoInfoByPoint(double lon, double lat);

}
