package com.iscas.smarthome.figures;

import com.iscas.smarthome.stateautomaton.attribute.Attribute;

public class TimeData {

    private String time;
    private Attribute attribute;

    public TimeData(String time, Attribute attribute) {
        this.time = time;
        this.attribute = attribute;
    }

    public TimeData() {}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return time + " " + attribute.toString();
    }
}
