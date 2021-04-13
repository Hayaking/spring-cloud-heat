package com.consumer.consumer.util;

import com.consumer.consumer.bean.dto.HeatDataDTO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MapUtil {
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKeyDesc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public static List<HeatDataDTO> getLastTimeMetric(List<HeatDataDTO> list) {
        List<HeatDataDTO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        Map<Date, List<HeatDataDTO>> datas = list.stream().collect(Collectors.groupingBy(HeatDataDTO::get__time));
        //倒序
        datas = MapUtil.sortByKeyDesc(datas);
        //取第一个
        result = datas.entrySet().stream()
                .findFirst()
                .get()
                .getValue();
        return result;
    }

}
