package com.iscas.smarthome.stateautomaton.graph;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.graphviz.GraphViz;
import com.iscas.smarthome.homeassistant.ServiceName;
import com.iscas.smarthome.specification.ModeMap;
import com.iscas.smarthome.specification.OuterEvent;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.state.InnerState;
import com.iscas.smarthome.stateautomaton.state.OuterState;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OuterGraph implements Graph {

    private static final long serialVersionUID = 1L;

    private List<OuterState> states;
    private OuterState preState = null;
    private String deviceName;
    private int time = 0;

    public OuterGraph(String deviceName) {
        states = new ArrayList<>();
        this.deviceName = deviceName;
    }

    public void addEdge(Edge edge) {
        edge.getSource().addOutNeighbor(edge);
        edge.getTarget().addInNeighbor(edge);
    }

    public void addState(OuterState state) {
        states.add(state);
    }

    public void initStates() {
        for(OuterState state : OuterEvent.getAllStates())
            addState(state);
    }

    public OuterState getPreState() {
        return preState;
    }

//    public void buildGraph(List<Data> input) {
//        OuterState preState = null;
//        int time = 0;
//
//        for(Data data : input) {
//            if(OuterEvent.containsEvent(data.getEvent())) {
//                OuterState target = OuterEvent.getState(data.getEvent());
//                time = 0;
//                if(preState != null) {
//                    Edge edge = new Edge(preState, target, data.getEvent());
//                    addEdge(edge);
//                    preState.leaveState(time);
//                }
//                target.addInnerState(new InnerState(time, data.getAttribute()));
//                preState = target;
//            } else if(InnerEvent.containsEvent(data.getEvent())) {
//                time += Constants.GET_ATTRIBUTE_TIME_GAP;
//                if(preState != null)
//                    preState.addInnerState(new InnerState(time, data.getAttribute()));
//            }
//        }
//    }

    public int callService() {
        if(preState == null)
            return 0;
        if(preState.serviceAllCalled())
            return new Random().nextInt(preState.getServiceSize());
        return preState.getUncalled();
    }

//    public void processData(Data data) {
//        String mode = data.getMode();
//        Attribute attribute = data.getAttribute();
//        if(!ModeMap.containsState(mode)) {
//            ModeMap.addState(mode, deviceName);
//            addState(ModeMap.getState(mode));
//        }
//
//        OuterState state = ModeMap.getState(mode);
//        if(state == preState) {
//            time += Constants.GET_ATTRIBUTE_TIME_GAP;
//        } else {
//            if(preState != null) {
//                Edge edge = new Edge(preState, state, ServiceName.preService);
//                addEdge(edge);
//                preState.leaveState(time);
//            }
//            time = 0;
//        }
//        state.addInnerState(new InnerState(time, attribute), deviceName);
//        preState = state;
//    }

    public void processData(Data data) {
        String mode = data.getMode();
        Attribute attribute = data.getAttribute();
        if(!ModeMap.containsState(mode)) {
            ModeMap.addState(mode, deviceName);
            addState(ModeMap.getState(mode));
        }

        OuterState state = ModeMap.getState(mode);
        if(state != preState) {
            if(preState != null) {
                Edge edge = new Edge(preState, state, ServiceName.preService);
                addEdge(edge);
            }
        }
        state.addInnerState(new InnerState(time, attribute), deviceName);
        preState = state;
    }

//    public void checkData(Data data) {
//        String mode = data.getMode();
//        Attribute attribute = data.getAttribute();
//        InnerState innerState = new InnerState(time, attribute);
//
//        OuterState state = ModeMap.getState(mode);
//        if(state == preState) {
//            time += Constants.GET_ATTRIBUTE_TIME_GAP;
//        } else {
//            time = 0;
//        }
//        if(state.checkNormal(innerState, deviceName)) {
//            System.out.println("data " + data.toString() + " is normal");
//        }
//    }

    public void checkData(OuterState state, List<Attribute> data) {
        if(state.checkNormal(data, deviceName))
            System.out.println("Current state is normal.");
    }

    public void print() {
        System.out.println("Current States");
        for(OuterState state : states) {
            System.out.println(state.toString());
        }
    }

    public void toGraph() {
        GraphViz graph = new GraphViz(Constants.GRAPH_DIR + deviceName + "/graph/", DateUtils.getDate());
        graph.startGraph();

        for(OuterState state : states) {
            for(Map.Entry<OuterState, Edge> entry : state.getOutNeighbors().entrySet()) {
                graph.add(state.getName() + "->" + entry.getKey().getName());
                String percent = String.format("%.1f", entry.getKey().getInNeighborPercent(state) * 100.0);
                graph.addLabel(entry.getValue().getEvent() + "(" + percent + "%)");
                graph.addln();
            }

            graph.startSubGraph("cluster" + state.getName());
            graph.addln();

            for(int i = 0; i < state.getInnerGraph().getStateSize(); i++) {
                graph.add(state.getName() + Integer.toString(i));
                String percent = String.format("%.1f", state.getInnerGraph().getLeavePercent(i) * 100.0);
                graph.addLabel(state.getInnerGraph().getState(i).getAttribute().toString() + "(" + percent + "%)");
                graph.addln();
                if(i != 0) {
                    graph.add(state.getName() + Integer.toString(i - 1) + "->" + state.getName() + Integer.toString(i));
                    graph.addLabel(Constants.GET_ATTRIBUTE_TIME_GAP + "s");
                    graph.addln();
                }
            }
            graph.endGraph();
            graph.addln();
            graph.add(state.getName() + "->" + state.getName() + "0");
            graph.addln();
        }
        graph.endGraph();
        graph.run();
    }

    public boolean hasFinishedTest(String deviceName) {
        //如果state都还不全，那肯定没完成
        if(states.size() < ServiceName.getServices(deviceName).size())
            return false;
        for(OuterState state : states) {
            if(!state.serviceAllCalled())
                return false;
        }
        return true;
    }
}
