package com.iscas.smarthome.stateautomaton.state;

import com.iscas.smarthome.stateautomaton.attribute.Attribute;

/**
 * 内部状态表示
 */
public class InnerState implements State{

    private String name;
    private int time;
    private Attribute attribute;

    public InnerState(int time) {
        this.time = time;
        this.name = Integer.toString(time);
        attribute = new Attribute();
    }

    public InnerState(int time, Attribute attribute) {
        this.time = time;
        this.name = Integer.toString(time);
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * 以衰减方式更新
     */
    public void mergeWithAttribute(InnerState state, double decay) {
        attribute.mergeWithOther(state.getAttribute(), decay);
    }

    @Override
    public String toString() {
        return name + ":" + attribute.toString();
    }
}
