package test;

import specification.ModeMap;
import stateautomaton.attribute.Attribute;
import stateautomaton.graph.OuterGraph;
import utils.Constants;
import utils.Timer;

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
                    graph.checkData(ModeMap.getState(entry.getKey()), entry.getValue());
                }
            }
        }
    }
}
