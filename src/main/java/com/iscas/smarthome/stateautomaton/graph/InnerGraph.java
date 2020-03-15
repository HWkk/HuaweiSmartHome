package com.iscas.smarthome.stateautomaton.graph;

import com.iscas.smarthome.data.CurveStatistics;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.state.InnerState;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.CurveUtils;

import java.util.ArrayList;
import java.util.List;

public class InnerGraph implements Graph {

    private List<InnerState> states;
    private List<Integer> leaveCounts;
    private int totalLeaveCount;

    public InnerGraph() {
        states = new ArrayList<>();
        leaveCounts = new ArrayList<>();
        totalLeaveCount = 0;
    }

    /**
     * 相对时间记录方式
     */
    public void addInnerStateByRelativeTime(InnerState state) {
        int index = state.getTime() / Constants.GET_ATTRIBUTE_TIME_GAP;
        if(index >= states.size()) {
            states.add(state);
            leaveCounts.add(0);
        } else {
            states.get(index).mergeWithAttribute(state, Constants.DECAY_FACTOR);
        }
    }

    /**
     * 绝对时间记录方式
     */
    public void addInnerStateByAbsoluteTime(InnerState state) {
        states.add(state);
    }

    public void addLeaveCount(int time) {
        int index = time / Constants.GET_ATTRIBUTE_TIME_GAP;
        totalLeaveCount++;
        leaveCounts.set(index, leaveCounts.get(index) + 1);
    }

    public double getLeavePercent(int index) {
        return totalLeaveCount == 0 ? 0.0 : ((double)leaveCounts.get(index) / totalLeaveCount);
    }

    public Attribute getAvg() {
        Attribute attribute = new Attribute();
        if(states.size() == 0) return attribute;

        for(InnerState state : states) {
            for(int i = 0; i < state.getAttribute().getDimension(); i++)
                attribute.setAttribute(i, attribute.getAttribute(i) + state.getAttribute().getAttribute(i));
        }

        for(int i = 0; i < attribute.getDimension(); i++)
            attribute.setAttribute(i, attribute.getAttribute(i) / states.size());
        return attribute;
    }

    public List<CurveStatistics> getCurveStatistics() {
        List<CurveStatistics> res = new ArrayList<>();
        if(states == null || states.size() == 0) return res;

        int dimension = states.get(0).getAttribute().getDimension();
        for(int i = 0; i < dimension; i++) {
            List<Double> curData = new ArrayList<>();
            for(InnerState state : states)
                curData.add(state.getAttribute().getAttribute(i));
            res.add(CurveUtils.calStatistics(curData));
        }
        return res;
    }

    public int getStateSize() {
        return states.size();
    }

    public InnerState getState(int index) {
        if(index >= states.size()) return states.get(states.size() - 1);
        return states.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(InnerState state : states)
            sb.append(state.toString() + " ");
        return sb.toString();
    }
}
