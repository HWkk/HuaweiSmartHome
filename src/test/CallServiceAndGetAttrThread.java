package test;

import data.Data;
import homeassistant.Caller;
import stateautomaton.graph.OuterGraph;
import utils.Constants;
import utils.Timer;

public class CallServiceAndGetAttrThread implements Runnable{

    String deviceName;
    OuterGraph graph;

    public CallServiceAndGetAttrThread(String deviceName, OuterGraph graph) {
        this.deviceName = deviceName;
        this.graph = graph;
    }

    @Override
    public void run() {
        while(true) {
            Caller.callService(deviceName);
            long start = System.currentTimeMillis();
            Timer.waitTimeGap(Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP);
            System.out.println("wait " + (System.currentTimeMillis() - start) + "ms to get attr");

            while((System.currentTimeMillis() - start) / 1000 < Constants.CALL_SERVICE_TIME_GAP) {
                Data data = Caller.getAttribute(deviceName);
                graph.processData(data);
                graph.print();
                graph.toGraph();
                Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
            }
        }
    }
}
