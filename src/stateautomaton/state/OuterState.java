package stateautomaton.state;

import homeassistant.AttributesName;
import stateautomaton.attribute.Attribute;
import stateautomaton.graph.Edge;
import stateautomaton.graph.InnerGraph;
import utils.Constants;

import java.util.HashMap;

public class OuterState implements State{

    private HashMap<OuterState, Edge> outNeighbors;
    private HashMap<OuterState, Integer> inNeighborsCount;
    private InnerGraph innerGraph;
    private String name;
    private int totalInCount;

    public OuterState(String name) {
        outNeighbors = new HashMap<>();
        inNeighborsCount = new HashMap<>();
        innerGraph = new InnerGraph();
        this.name = name;
        totalInCount = 0;
    }

    public HashMap<OuterState, Edge> getOutNeighbors() {
        return outNeighbors;
    }

    public HashMap<OuterState, Integer> getInNeighborsCount() {
        return inNeighborsCount;
    }

    public InnerGraph getInnerGraph() {
        return innerGraph;
    }

    public String getName() {
        return name;
    }

    public int getTotalInCount() {
        return totalInCount;
    }

    public void addOutNeighbor(Edge e) {
        if(!outNeighbors.containsKey(e.getTarget()))
            outNeighbors.put(e.getTarget(), e);
    }

    public void addInNeighbor(Edge e) {
        inNeighborsCount.put(e.getSource(), inNeighborsCount.getOrDefault(e.getSource(), 0) + 1);
        totalInCount++;
    }

    public double getInNeighborPercent(OuterState state) {
        return (double)inNeighborsCount.getOrDefault(state, 0) / totalInCount;
    }

    public void addInnerState(InnerState state, String deviceName) {
//        if(checkNormal(state, deviceName))
            innerGraph.addInnerState(state);
    }

    public boolean checkNormal(InnerState state, String deviceName) {
        if(innerGraph.getStateSize() == 0) return true;
        Attribute avg = innerGraph.getAvg();

        boolean normal = true;
        for(int i = 0; i < avg.getDimension(); i++) {
            double curAttr = state.getAttribute().getAttribute(i);
            double avgAttr = avg.getAttribute(i);
            if(Math.abs(curAttr - avgAttr) / avgAttr > Constants.ABNORMAL_THRESHOLD) {
                normal = false;
                String attrName = AttributesName.getAttributes(deviceName).get(i);
                StringBuilder sb = new StringBuilder(attrName + " is too ");
                sb.append(curAttr > avgAttr ? "high!" : "low!");
                System.out.println(sb.toString());
            }
        }
        return normal;
    }

    public void leaveState(int time) {
        innerGraph.addLeaveCount(time);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OuterState)) return false;
        OuterState state = (OuterState) o;
        return name != null ? name.equals(state.name) : state.name == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name + "\n");
        sb.append("neighbors: ");
        for(OuterState neighbor : outNeighbors.keySet())
            sb.append(neighbor.getName() + "\t");
        sb.append("\n");
        sb.append("innerStates: ");
        sb.append(innerGraph.toString() + "\n");
        return sb.toString();
    }
}
