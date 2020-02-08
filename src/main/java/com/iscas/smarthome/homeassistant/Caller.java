package com.iscas.smarthome.homeassistant;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;

import java.util.List;
import java.util.Random;

public class Caller {

    private static int preIndex = -1;

    public static void getAllAttributes(String deviceName) {
        HAScript.getAllAttributes(deviceName);
    }

    public static Data getAttribute(String deviceName) {
        return HAScript.getAttribute(deviceName);
    }

    public static void getAllService() {
        HAScript.getAllServices();
    }

    public static void callService(String deviceName) {
        List<String> services = ServiceName.getServices(deviceName);
        Random random = new Random();
        int index = -1;

        while(true) {
            index = random.nextInt(services.size());
            if(index != preIndex) break;
        }
        preIndex = index;
        HAScript.callService(deviceName, services.get(index));
    }

    public static void callService(String deviceName, OuterGraph graph) {
        List<String> services = ServiceName.getServices(deviceName);
        int index = -1;

        while(true) {
            index = graph.callService();
            if(index != preIndex) break;
        }
        preIndex = index;
        HAScript.callService(deviceName, services.get(index));
    }

    public static OuterGraph init(String deviceName, int getAttrTimeGap, int callServiceTimeGap, int getAttrAfterCallingTimeGap) {
        Constants.DEVICE_NAME = deviceName;
        Constants.GET_ATTRIBUTE_TIME_GAP = getAttrTimeGap;
        Constants.CALL_SERVICE_TIME_GAP = callServiceTimeGap;
        Constants.GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP = getAttrAfterCallingTimeGap;
        return new OuterGraph(deviceName);
    }
}
