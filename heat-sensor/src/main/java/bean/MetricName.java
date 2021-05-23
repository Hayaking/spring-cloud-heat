package bean;

public enum MetricName {
    component_up( "健康度", "" ),
    pipeline_water_temperature("管道温度","℃"),
    pipeline_water_temperature_increase(" 管道温度增速","℃/minute"),
    pipeline_out_temperature("管道外温度","℃"),
    pipeline_water_flow("管道流量","m³"),
    pipeline_water_tassels("管道流速","m/minute"),
    pipeline_water_level("管道水位","cm"),
    pipeline_water_pressure("管道压力","MPa"),
    pipeline_water_pressure_increase("管道压力增速","MPa/minute"),
    pipeline_valve_state("管道阀门开度",""),

    station_water_temperature("热站水温","℃"),
    station_water_flow("热站流量","m³"),
    station_water_pressure("热站水压","MPa"),
    station_water_tassels("热站流速","m/minute"),
    station_temperature_increase("热站水温增速","℃/minute"),
    station_water_pressure_increase("热站水压增速","MPa/minute"),
    station_temperature("热站室温","℃"),
    station_valve_state("热站阀门开度",""),

    pump_watt("功率","w"),
    pump_voltage("电压","v"),
    pump_state("状态",""),

    sensor_e_quantity("电量","%"),
    sensor_state("状态",""),
    sensor_frequency("上报频率",""),
    sensor_up_time("运行时长",""),
    ;

    private final String aliasName;
    private final String unit;

    MetricName(String s,String unit) {
        this.aliasName = s;
        this.unit = unit;
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getUnit() {
        return unit;
    }
}
