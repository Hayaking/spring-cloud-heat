package com.consumer.consumer.bean.enu;

public enum MetricEnum {

    pipeline_water_temperature("供水温度"),
    pipeline_water_flow("供水流量"),
    pipeline_water_pressure("供水压力"),
    pipeline_water_tassels("供水流速"),
    pipeline_water_temperature_increase(" 供水温度增速"),
    pipeline_water_pressure_increase("供水压力增速"),
    pipeline_water_level("管道水位"),
    pipeline_out_temperature("管道外温度"),
    pipeline_valve_state("阀门开度"),
    ;

    private final String describe;

    MetricEnum(String s) {
        this.describe = s;
    }

    public static String getMetricChName(String name) {
        for (MetricEnum value : MetricEnum.values()) {
            if (value.name().equals(name)) {
                return value.describe;
            }
        }
        return "";
    }
}
