package com.consumer.consumer.bean.enu;

public enum MetricEnum {

    component_up("健康度"),
    pipeline_water_temperature("管道温度"),
    pipeline_water_flow("管道流量"),
    pipeline_water_pressure("管道压力"),
    pipeline_water_tassels("管道流速"),
    pipeline_water_temperature_increase(" 管道温度增速"),
    pipeline_water_pressure_increase("管道压力增速"),
    pipeline_water_level("管道水位"),
    pipeline_out_temperature("管道外温度"),
    pipeline_valve_state("管道阀门开度"),

    station_water_temperature("热站水温"),
    station_water_flow("热站流量"),
    station_water_pressure("热站水压"),
    station_water_tassels("热站流速"),
    station_temperature_increase("热站水温增速"),
    station_water_pressure_increase("热站水压增速"),
    station_temperature("热站室温"),
    station_valve_state("热站阀门开度"),

    pump_watt("功率"),
    pump_voltage("电压"),
    pump_state("状态"),

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
