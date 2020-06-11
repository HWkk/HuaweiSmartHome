package com.iscas.smarthome.stateautomaton.graph;

import com.iscas.smarthome.stateautomaton.state.OuterState;

import java.io.Serializable;

/**
 * 模型外部边表示
 */
public class Edge implements Serializable {

    private OuterState source;
    private OuterState target;
    private String event;

    public Edge(OuterState source, OuterState target, String event) {
        this.source = source;
        this.target = target;
        this.event = event;
    }

    public OuterState getSource() {
        return source;
    }

    public void setSource(OuterState source) {
        this.source = source;
    }

    public OuterState getTarget() {
        return target;
    }

    public void setTarget(OuterState target) {
        this.target = target;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
