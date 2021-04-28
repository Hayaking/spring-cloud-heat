package com.consumer.consumer.util;

import com.consumer.consumer.bean.vo.HeatMapData;

import java.util.LinkedList;
import java.util.List;

public class LineUtil {

    public static List<HeatMapData> addPoint(double x1, double y1, double x2, double y2, double value) {
        List<HeatMapData> result = new LinkedList<>();
        double minX = Math.min(x1, x2);
        double disX = Math.abs(x1 - x2);

        double minY = Math.min(y1, y2);
        double disY = Math.abs(y1 - y2);
        // 需要补的点的个数
        double num = Math.max(disX / 0.0001, disY / 0.0001);
        double metaX = disX / num;
        double metaY = disY / num;
        for (int i = 0; i < num; i++) {
            HeatMapData data = new HeatMapData();
            data.setLat(minX + i * metaX);
            data.setLng(minY + i * metaY);
            data.setCount(value);
            result.add(data);
        }
        return result;
    }
}
