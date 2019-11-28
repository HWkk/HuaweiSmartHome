package stateautomaton.graph;

import data.Data;
import graphviz.GraphViz;
import homeassistant.ServiceName;
import specification.ModeMap;
import specification.OuterEvent;
import stateautomaton.attribute.Attribute;
import stateautomaton.state.InnerState;
import stateautomaton.state.OuterState;
import utils.Constants;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OuterGraph implements Graph {

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

    public void processData(Data data) {
        String mode = data.getMode();
        Attribute attribute = data.getAttribute();
        if(!ModeMap.containsState(mode)) {
            ModeMap.addState(mode);
            addState(ModeMap.getState(mode));
        }

        OuterState state = ModeMap.getState(mode);

        if(state == preState) {
            time += Constants.GET_ATTRIBUTE_TIME_GAP;
        } else {
            if(preState != null) {
                Edge edge = new Edge(preState, state, ServiceName.preService);
                addEdge(edge);
                preState.leaveState(time);
            }
            time = 0;
        }
        state.addInnerState(new InnerState(time, attribute), deviceName);
        preState = state;
    }

    public void print() {
        System.out.println("Current States");
        for(OuterState state : states) {
//            if(state.getOutNeighbors().size() != 0)
                System.out.println(state.toString());
        }
    }

    public void toGraph() {
        GraphViz graph = new GraphViz(Constants.graphDir + deviceName + "/graph/", DateUtils.getDate());
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

//    public boolean hasFinishedTest() {
//
//    }
}
