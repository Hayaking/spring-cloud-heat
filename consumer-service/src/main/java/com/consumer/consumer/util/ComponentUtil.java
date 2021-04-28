package com.consumer.consumer.util;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class ComponentUtil {
    public static final List<Integer> PIPE_TYPE = asList(1, 2, 3, 4, 5);
    public static final List<Integer> STATION_TYPE = Collections.singletonList(6);
    public static final List<Integer> PUMP_TYPE = asList(7, 8);
    public static final List<Integer> SENSOR_TYPE = asList(9, 10, 11);

    public static boolean isPipe(int type) {
        return PIPE_TYPE.contains(type);
    }

    public static boolean isStation(int type) {
        return STATION_TYPE.contains(type);
    }

    public static boolean isPump(int type) {
        return PUMP_TYPE.contains(type);
    }

    public static boolean isSensor(int type) {
        return SENSOR_TYPE.contains(type);
    }
}
