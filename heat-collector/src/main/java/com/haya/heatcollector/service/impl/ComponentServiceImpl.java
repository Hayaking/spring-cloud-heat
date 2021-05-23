package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.mapper.ComponentMapper;
import com.haya.heatcollector.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haya
 */
@Service
@CacheConfig(cacheNames = "component")
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {
    @Autowired
    private ComponentMapper componentMapper;
    private static final Map<Integer, String> TYPE_MAP = new ConcurrentHashMap<>( 6 );
    static {
        TYPE_MAP.put( 1, "温度" );
        TYPE_MAP.put( 2, "流量" );
        TYPE_MAP.put( 3, "压力" );
        TYPE_MAP.put( 4, "阀门" );
        TYPE_MAP.put( 5, "泵" );
    }
    @Override
    @Cacheable(key = "#data.type +':' + #data.lon+':'+#data.lat", condition = "#result!=null")
    public Component selectElseInsert(HeatData data) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "lon", data.getLon() )
                .eq( "lat", data.getLat() )
                .eq( "type", data.getType() );
        Component res = componentMapper.selectOne( wrapper );
        if (res == null) {
            Component component = new Component();
            component.setLat( data.getLat() );
            component.setLon( data.getLon() );
            component.setType( data.getType() );
            component.setArea( data.getArea() );
            component.setStreet( data.getStreet() );
            component.setAddress( data.getAddress() );
            component.setCtime( new Date() );
            String namePrefix = TYPE_MAP.get( data.getType() );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            component.setName( namePrefix + '-' + LocalDateTime.now().format( formatter ) );
            componentMapper.insert( component );
            res = componentMapper.selectOne( wrapper );
        } else {
            res.setLat( data.getLat() );
            res.setLon( data.getLon() );
            res.setType( data.getType() );

            componentMapper.updateById( res );
        }
        return res;
    }
}
