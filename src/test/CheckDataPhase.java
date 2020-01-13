package test;

import data.Data;
import homeassistant.Caller;
import specification.ModeMap;
import stateautomaton.graph.OuterGraph;
import stateautomaton.state.OuterState;
import utils.Constants;
import utils.FileUtils;
import utils.Timer;

import java.util.HashMap;
import java.util.Random;

public class CheckDataPhase implements Runnable{

    String deviceName;
    OuterGraph graph;

    public CheckDataPhase(String deviceName) {
        this.deviceName = deviceName;
        this.graph = (OuterGraph) FileUtils.readFromFile(Constants.GRAPH_DIR + deviceName + "/model1");
        ModeMap.setMap((HashMap<String, OuterState>)FileUtils.readFromFile(Constants.GRAPH_DIR + deviceName + "/modeMap1"));
    }

    @Override
    public void run() {
        while(true) {
            Caller.callService(deviceName, graph);
            long start = System.currentTimeMillis();
            Timer.waitTimeGap(Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP);
            System.out.println("wait " + (System.currentTimeMillis() - start) + "ms to get attr");

            // 下次调用服务的时间间隔
            // 下限为：获取一次属性之后；上限为：nextServiceCallGap。
            int lowerBound = Constants.GET_ATTRIBUTE_TIME_GAP + Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP;
            int upperBound = Constants.CALL_SERVICE_TIME_GAP;
            int nextServiceCallGap = new Random().nextInt(upperBound - lowerBound) + lowerBound;

            while((System.currentTimeMillis() - start) / 1000 < nextServiceCallGap) {
                Data data = Caller.getAttribute(deviceName);
                graph.checkData(data);
                Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
            }
        }
    }
}
