package com.iscas.smarthome.test;

import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckThread implements Runnable{

    OuterGraph graph;
    HashMap<String, List<Attribute>> checkData;

    public CheckThread(OuterGraph graph, HashMap<String, List<Attribute>> checkData) {
        this.graph = graph;
        this.checkData = checkData;
    }

    @Override
    public void run() {
        while(true) {
            Timer.waitTimeGap(Constants.CHECK_TIME_GAP);
            synchronized (checkData) {
                for(Map.Entry<String, List<Attribute>> entry : checkData.entrySet()) {
//                    graph.checkData(ModeMap.getState(entry.getKey()), entry.getValue());
                }
            }
        }
    }
}
