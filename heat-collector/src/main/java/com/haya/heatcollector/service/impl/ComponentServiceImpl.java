package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.mapper.ComponentMapper;
import com.haya.heatcollector.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author haya
 */
@Service
@CacheConfig(cacheNames = "component")
public class ComponentServiceImpl
        extends ServiceImpl<ComponentMapper, Component>
        implements ComponentService {
    @Autowired
    private ComponentMapper componentMapper;


    @CachePut(key = "#component.cacheKey",condition = "result!=null")
    @Override
    public Component insertToCache(Component component) {
        componentMapper.insert( component );
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "lon", component.getLon() )
                .eq( "lat", component.getLat() );
        return componentMapper.selectOne( wrapper );
    }

    @Override
    @Cacheable(key = "#data.lon+':'+#data.lat", condition = "#result!=null")
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
            componentMapper.insert( component );
            res = componentMapper.selectOne( wrapper );
        }
        return res;
    }
}
