package bean;

public enum ComponetType {
    TemperatureSensor( 1, "温度传感器" ),
    FlowSensor( 2, "流量传感器" ),
    PressureSensor( 3, "压力传感器" ),
    ValveSensor( 4, "阀门传感器" ),
    PummpSensor( 5, "泵传感器" ),
    ;

    private int type;
    private String note;

    ComponetType(int type, String note) {
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
