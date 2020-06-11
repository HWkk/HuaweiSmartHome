package com.iscas.smarthome.utils;

import com.iscas.smarthome.figures.PythonFigureUtils;
import com.iscas.smarthome.figures.TimeData;
import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.specification.ModeMap;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.graph.InnerGraph;
import com.iscas.smarthome.stateautomaton.state.OuterState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataUtils {

    public static List<TimeData> getModeDataFromModel(String mode) {
        List<TimeData> res = new ArrayList<>();
        OuterState outerState = ModeMap.getState(mode);
        InnerGraph graph = outerState.getInnerGraph();

        int time = 0;
        for(int i = 0; i < graph.getStateSize(); i++) {
            TimeData data = new TimeData(Integer.toString(time), graph.getState(i).getAttribute());
            res.add(data);
            time += Constants.GET_ATTRIBUTE_TIME_GAP;
        }
        return res;
    }

    public static List<TimeData> getModeDataFromCheck(String mode, HashMap<String, List<Attribute>> checkData) {
        List<TimeData> res = new ArrayList<>();
        List<Attribute> attributes = checkData.get(mode);

        int time = 0;
        for(int i = 0; i < attributes.size(); i++) {
            TimeData data = new TimeData(Integer.toString(time), attributes.get(i));
            res.add(data);
            time += Constants.GET_ATTRIBUTE_TIME_GAP;
        }
        return res;
    }

    public static void getModeAttrFigure(String mode, HashMap<String, List<Attribute>> map, String entityId, String saveFile) {
        List<TimeData> modelData = getModeDataFromModel(mode);
        List<TimeData> checkData = getModeDataFromCheck(mode, map);

        List<String> legends = new ArrayList<>();
        legends.add(mode + "-model-data");
        if(checkData.size() > 1)
            legends.add(mode + "-real-time-data");

        List<List<TimeData>> data = new ArrayList<>();
        data.add(modelData);
        if(checkData.size() > 1) data.add(checkData);

        PythonFigureUtils.drawLineChartTogether(legends, data, entityId, saveFile);
    }

    public static HashMap<String, List<String>> getAllModeAttrFigure(HashMap<String, List<Attribute>> map, String entityId) {
        List<String> attributes = AttributesName.getAttributes(entityId);
        HashMap<String, List<String>> locations = new HashMap<>();

        for(String mode : map.keySet()) {
            String date = DateUtils.getDate();
            String fileName = "/img/attribute/" + entityId + "/" + mode + "/" + date;
            List<String> loc = new ArrayList<>();
            for(int i = 0; i < attributes.size(); i++) {
                loc.add(fileName + "_" + attributes.get(i) + ".png");
            }
            locations.put(mode, loc);
            try {
                Runtime.getRuntime().exec("mkdir -p " + Constants.ATTRIBUTE_PNG_DIR + entityId + "/" + mode + "/").waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getModeAttrFigure(mode, map, entityId, Constants.ATTRIBUTE_PNG_DIR + entityId + "/" + mode + "/" + date);
        }
        return locations;
    }
}
