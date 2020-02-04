package com.iscas.smarthome.test;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.specification.ModeMap;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.FileUtils;
import com.iscas.smarthome.utils.Timer;

import java.util.Random;

public class BuildGraphPhase implements Runnable{

    String deviceName;
    OuterGraph graph;

    public BuildGraphPhase(String deviceName, OuterGraph graph) {
        this.deviceName = deviceName;
        this.graph = graph;
    }

    @Override
    public void run() {
//        int i = 0;
        while(!graph.hasFinishedTest(deviceName)) {
//        while(i++ < 3) {
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
                graph.processData(data);
                graph.print();
                graph.toGraph();
                Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
            }
        }
        System.out.println("Test Finished. Model Completed.");
        FileUtils.saveToFile(Constants.GRAPH_DIR + deviceName + "/model1", graph);
        FileUtils.saveToFile(Constants.GRAPH_DIR + deviceName + "/modeMap1", ModeMap.getMap());
    }
}
