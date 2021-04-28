package bean;

public enum MetricType {
    SUPPLY_PIPE( 1, "一次供水管道" ),
    BACK_PIPE( 2, "一次回水管道" ),
    SUPPLY_SECONDARY_PIPE( 3, "二次供水管道" ),
    BACK_SECONDARY_PIPE( 4, "二次回水管道" ),
    CIRCULATION_PIPE( 5, "循环管道" ),
    STATION(6, "热交换站"),
    CIRCLE_PUMP(7, "循环泵"),
    MAKE_PUMP(8, "补水泵"),
    SENSOR_PRESS(9,"压力传感器"),
    SENSOR_TEMP(10,"温度传感器"),
    SENSOR_FLOW(11,"流量传感器"),
    ;

    private int type;
    private String note;

    MetricType(int type, String note) {
        this.type = type;
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
