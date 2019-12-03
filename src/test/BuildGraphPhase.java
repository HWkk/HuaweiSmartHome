package test;

import data.Data;
import homeassistant.Caller;
import specification.ModeMap;
import stateautomaton.graph.OuterGraph;
import utils.Constants;
import utils.FileUtils;
import utils.Timer;

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
        FileUtils.saveToFile(Constants.graphDir + deviceName + "/model", graph);
        FileUtils.saveToFile(Constants.graphDir + deviceName + "/modeMap", ModeMap.getMap());
    }
}