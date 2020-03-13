package com.iscas.smarthome.stateautomaton.state;

import com.iscas.smarthome.data.CurveStatistics;
import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.homeassistant.ServiceName;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.graph.Edge;
import com.iscas.smarthome.stateautomaton.graph.InnerGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.CurveUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

public class OuterState implements State, Externalizable {

    private HashMap<OuterState, Edge> outNeighbors;
    private HashMap<OuterState, Integer> inNeighborsCount;
    private InnerGraph innerGraph;
    private String name;
    private int totalInCount;
    private boolean[] serviceCalled;

    public OuterState(String name, String deviceName) {
        outNeighbors = new HashMap<>();
        inNeighborsCount = new HashMap<>();
        innerGraph = new InnerGraph();
        this.name = name;
        totalInCount = 0;
        serviceCalled = new boolean[ServiceName.getServices(deviceName).size()];
    }

    public OuterState() {
        outNeighbors = null;
        inNeighborsCount = null;
        innerGraph = null;
        name = null;
        totalInCount = 0;
        serviceCalled = null;
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
        innerGraph.addInnerStateByRelativeTime(state);
//        innerGraph.addInnerStateByAbsoluteTime(state);
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
                sb.append(" Most possible reason is: " + findAbnormalReason(i, state));
                System.out.println(sb.toString());
            }
        }
        return normal;
    }

    public boolean checkNormal(List<Attribute> data, String deviceName) {
        if(innerGraph.getStateSize() == 0) return true;
        List<CurveStatistics> normalStatistics = innerGraph.getCurveStatistics();

        boolean normal = true;
        for(int i = 0; i < normalStatistics.size(); i++) {
            List<Double> curAttr = new ArrayList<>();
            String attrName = AttributesName.getAttributes(deviceName).get(i);
            for(Attribute attribute : data)
                curAttr.add(attribute.getAttribute(i));
            double valueMedian = CurveUtils.calValueMedian(curAttr), slopeMedian = CurveUtils.calSlopeMedian(curAttr);
            if(normalStatistics.get(i).getSlopeIQR().isOutlier(slopeMedian) || normalStatistics.get(i).getValueIQR().isOutlier(valueMedian)) {
                normal = false;
                System.out.println(attrName + " is abnormal.");
                //TODO: 异常原因还要加上
            }
        }
        return normal;
    }

    public String findAbnormalReason(int attrIndex, InnerState state) {
        InnerState pre = null, curInnerState = state;
        OuterState curOuter = this;
        int index = state.getTime() / curOuter.getInnerGraph().getStateSize();
        if(index >= curOuter.getInnerGraph().getStateSize())
            index = curOuter.getInnerGraph().getStateSize() - 1;
        double maxInfluence = 0.0;
        String event = "";

        for(int i = 0; i < Constants.BACK_COUNT; i++) {
            String curEvent = "";
            if(index == 0) {
                pre = curInnerState;
                OuterState newOuter = curOuter.findMaxPreOuter();
                index = newOuter.findMaxInner();
                curInnerState = newOuter.getInnerGraph().getState(index);
                curEvent = "Operation " + newOuter.getOutNeighbors().get(curOuter).getEvent();
                curOuter = newOuter;
            } else {
                pre = curInnerState;
                curInnerState = curOuter.getInnerGraph().getState(--index);
                curEvent = "Working too long or sudden changes in environment.";
            }
            double influence = Math.abs(pre.getAttribute().getAttribute(attrIndex) - curInnerState.getAttribute().getAttribute(attrIndex));
            if (influence > maxInfluence) {
                maxInfluence = influence;
                event = curEvent;
            }
        }
        return event;
    }

    public OuterState findMaxPreOuter() {
        OuterState res = null;
        int maxFrequency = 0;
        for(Map.Entry<OuterState, Integer> entry : inNeighborsCount.entrySet()) {
            if(entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                res = entry.getKey();
            }
        }
        return res;
    }

    public int findMaxInner() {
        int res = 0;
        double maxPercentage = 0.0;
        for(int i = 0; i < innerGraph.getStateSize(); i++) {
            double p = innerGraph.getLeavePercent(i);
            if(p > maxPercentage) {
                maxPercentage = p;
                res = i;
            }
        }
        return res;
    }

    public void leaveState(int time) {
        innerGraph.addLeaveCount(time);
    }

    public boolean serviceAllCalled() {
        for(boolean b : serviceCalled)
            if(!b) return false;
        return true;
    }

    public int getUncalled() {
        int index = 0;
        Random rand = new Random();
        while(serviceCalled[index]) {
            index = rand.nextInt(serviceCalled.length);
        }
        serviceCalled[index] = true;
        return index;
    }

    public int getServiceSize() {
        return serviceCalled.length;
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

    @Override
    public void writeExternal(final ObjectOutput o) throws IOException {
        o.writeUTF(name);
        o.writeInt(totalInCount);
        o.writeObject(innerGraph);
        o.writeObject(serviceCalled);
        o.writeObject(inNeighborsCount);
        o.writeObject(outNeighbors);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(final ObjectInput o) throws IOException, ClassNotFoundException {
        name = o.readUTF();
        totalInCount = o.readInt();
        innerGraph = (InnerGraph)o.readObject();
        serviceCalled = (boolean[])o.readObject();
        inNeighborsCount = (HashMap<OuterState, Integer>)o.readObject();
        outNeighbors = (HashMap<OuterState, Edge>)o.readObject();
    }
}
