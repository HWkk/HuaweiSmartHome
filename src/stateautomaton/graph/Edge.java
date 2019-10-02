package stateautomaton.graph;

import stateautomaton.state.OuterState;

public class Edge {

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
