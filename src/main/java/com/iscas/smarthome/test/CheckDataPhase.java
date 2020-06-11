package com.iscas.smarthome.test;

import com.iscas.smarthome.utils.DataUtils;
import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.specification.ModeMap;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.stateautomaton.state.OuterState;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.FileUtils;
import com.iscas.smarthome.utils.Timer;
import com.iscas.smarthome.websocket.CustomWebSocket;

import java.util.*;

public class CheckDataPhase implements Runnable{

    String deviceName;
    OuterGraph graph;
    HashMap<String, List<Attribute>> checkData; //key为模式，value为当前模式下的收集的取值
    CustomWebSocket webSocket;
    int step = 0;

    public CheckDataPhase(String deviceName) {
        this.deviceName = deviceName;
        this.graph = (OuterGraph) FileUtils.readFromFile(Constants.GRAPH_DIR + deviceName + "/model1");
        ModeMap.setMap((HashMap<String, OuterState>)FileUtils.readFromFile(Constants.GRAPH_DIR + deviceName + "/modeMap1"));

        checkData = new HashMap<>();
        for(String mode : ModeMap.getMap().keySet())
            checkData.put(mode, new ArrayList<Attribute>());
//        new Thread(new CheckThread(graph, checkData)).start();
    }

    public CheckDataPhase(String deviceName, OuterGraph graph, CustomWebSocket webSocket) {
        this.deviceName = deviceName;
        this.graph = graph;

        checkData = new HashMap<>();
        for(String mode : ModeMap.getMap().keySet())
            checkData.put(mode, new ArrayList<Attribute>());
        this.webSocket = webSocket;
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
                step++;

                //在CheckThread里要读checkData，在此处要写checkData，需要加锁
                //TODO: 需要检测正确性
//                synchronized (checkData) {
                checkData.get(data.getMode()).add(data.getAttribute());
//                }
//                if(step >= Constants.CHECK_DATA_MIN_THRESHOLD)
//                    graph.checkData(ModeMap.getState(data.getMode()), checkData.get(data.getMode()), webSocket);
                if(step % Constants.CHECK_STEP_GAP == 0) {
                    StringBuilder message = new StringBuilder("C:");
                    HashMap<String, List<String>> locations = DataUtils.getAllModeAttrFigure(checkData, deviceName);
                    for (Map.Entry<String, List<String>> entry : locations.entrySet())
                        message.append(entry.getKey() + entry.getValue().toString() + "+");
                    message.deleteCharAt(message.length() - 1);
                    webSocket.sendAllMessage(message.toString());

                    graph.checkData(ModeMap.getState(data.getMode()), checkData.get(data.getMode()), webSocket);
                }
                Timer.waitTimeGap(Constants.GET_ATTRIBUTE_TIME_GAP);
            }
        }
    }
}
