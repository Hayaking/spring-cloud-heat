package com.haya.heatcollector.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haya.heatcollector.bean.BaiduGeoInfo;
import com.haya.heatcollector.service.BaiduGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author haya
 */
@CacheConfig(cacheNames = "geo")
@Service
public class BaiduGeoServiceImpl implements BaiduGeoService {
    @Autowired
    private RestTemplate restTemplate;
    String url = "https://api.map.baidu.com/reverse_geocoding/v3/?output=json&ak=7oiIULIcsnPvsc2TYHawfcI7UfQM29VH&location=";


    @Override
    @Cacheable(key = "#lon+':'+#lat")
    public BaiduGeoInfo getGeoInfoByPoint(double lon, double lat) {
        System.out.println("http for geo");
        ResponseEntity<String> exchange = restTemplate.getForEntity( url + lat + ',' + lon, String.class );
        JSONObject response = JSON.parseObject( exchange.getBody() );
        if (ObjectUtils.isEmpty( response )) {
            return new BaiduGeoInfo();
        }
        JSONObject result = response.getJSONObject( "result" );
        if (ObjectUtils.isEmpty( result )) {
            return new BaiduGeoInfo();
        }
        return BaiduGeoInfo.builder()
                .city( result.getJSONObject( "addressComponent" ).getString( "city" ) )
                .area( result.getJSONObject( "addressComponent" ).getString( "district") )
                .address( result.getString( "formatted_address" ) )
                .streetNumber( result.getJSONObject( "addressComponent" ).getString( "street_number" ) )
                .street( result.getJSONObject( "addressComponent" ).getString( "street" ) )
                .build();
    }
}
