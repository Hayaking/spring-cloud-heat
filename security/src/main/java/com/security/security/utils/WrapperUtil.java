package com.security.security.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.security.bean.ComponentFilter;
import com.security.security.bean.Item;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

public class WrapperUtil {

    public static void buildCondition(ComponentFilter filter, QueryWrapper<?> wrapper) {
        if (!CollectionUtils.isEmpty( filter.getData() )) {
            filter.getData()
                    .stream()
                    .collect( Collectors.groupingBy( Item::getKey ) )
                    .forEach( (name, dataList) -> {
                        wrapper.and( w -> {
                            for (int i = 0; i < dataList.size(); i++) {
                                Item data = dataList.get( i );
                                if ("level".equals( name )) {
                                    w.eq( name, data.getValue() );
                                } else {
                                    w.like( name, data.getValue() );
                                }
                                if (i != dataList.size() - 1) {
                                    w.or();
                                }
                            }
                        } );
                    } );
        }
    }
}
