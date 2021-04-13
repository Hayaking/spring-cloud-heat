package com.consumer.consumer.bean.enu;

public enum ComponentEnum {
    SUPPLY_PIPE( 1, "一次供水管道" ),
    BACK_PIPE( 2, "一次回水管道" ),
    SUPPLY_SECONDARY_PIPE( 3, "二次供水管道" ),
    BACK_SECONDARY_PIPE( 4, "二次回水管道" ),
    CIRCULATION_PIPE( 5, "循环管道" ),
    ;

    private int type;
    private String note;

    ComponentEnum(int type, String note) {
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
