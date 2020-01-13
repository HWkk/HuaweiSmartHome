package test;

import data.Data;
import homeassistant.Caller;
import stateautomaton.graph.OuterGraph;
import utils.Constants;
import utils.Timer;

public class GetAttributeThread implements Runnable{

    String deviceName;
    OuterGraph graph;

    public GetAttributeThread(String deviceName, OuterGraph graph) {
        this.deviceName = deviceName;
        this.graph = graph;
    }

    public void run() {
        while(true) {
            Data data = Caller.getAttribute(deviceName);
//            graph.processData(data);
//            graph.print();
//            graph.toGraph();
            Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
        }
    }
}
