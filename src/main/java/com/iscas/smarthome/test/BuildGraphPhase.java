package com.iscas.smarthome.test;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.specification.ModeMap;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.FileUtils;
import com.iscas.smarthome.utils.Timer;
import com.iscas.smarthome.websocket.CustomWebSocket;

import java.util.Random;

/**
 * 构建模型的线程
 */
public class BuildGraphPhase implements Runnable{

    String deviceName;
    OuterGraph graph;
    CustomWebSocket webSocket;

    public BuildGraphPhase(String deviceName, OuterGraph graph, CustomWebSocket webSocket) {
        this.deviceName = deviceName;
        this.graph = graph;
        this.webSocket = webSocket;
    }

    @Override
    public void run() {
        int i = 0;
        //当模型未收敛时
//        while(!graph.hasFinishedTest(deviceName)) {
        while(i++ < 5) {
            //调用操作
            Caller.callService(deviceName, graph);
            long start = System.currentTimeMillis();
            Timer.waitTimeGap(Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP);
            System.out.println("wait " + (System.currentTimeMillis() - start) + "ms to get attr");


            // 下次调用服务的时间间隔
            // 下限为：获取一次属性之后；上限为：nextServiceCallGap。
            int lowerBound = 2 * Constants.GET_ATTRIBUTE_TIME_GAP + Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP;
            int upperBound = Constants.CALL_SERVICE_TIME_GAP;
            int nextServiceCallGap = new Random().nextInt(upperBound - lowerBound) + lowerBound;
//            int nextServiceCallGap = Constants.CALL_SERVICE_TIME_GAP;

            while((System.currentTimeMillis() - start) / 1000 < nextServiceCallGap) {
                //获取属性
                Data data = Caller.getAttribute(deviceName);
                //处理属性数据
                graph.processDataByRelativeTime(data);
//                graph.processDataByAbsoluteTime(data);
                graph.print();
                String fileLoc = graph.toGraph();
                String fileName = fileLoc.substring(fileLoc.lastIndexOf("/") + 1);
                //将本地实时生成的图片复制到项目内，用于系统展示
                FileUtils.copyFile(fileLoc, Constants.MODEL_PNG_DIR + deviceName + "/", fileName);
                webSocket.sendAllMessage("M:/img/model/" + deviceName + "/" + fileName);
                Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
            }
        }
        System.out.println("Test Finished. Model Completed.");
        webSocket.sendAllMessage("M:FinishModel");

        //将模型以序列化方式存储到文件
        FileUtils.saveToFile(Constants.GRAPH_DIR + deviceName + "/model1", graph);
        FileUtils.saveToFile(Constants.GRAPH_DIR + deviceName + "/modeMap1", ModeMap.getMap());
    }
}
