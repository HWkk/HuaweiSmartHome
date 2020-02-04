package com.iscas.smarthome.test;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.Timer;

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
