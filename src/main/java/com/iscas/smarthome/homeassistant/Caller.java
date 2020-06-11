package com.iscas.smarthome.homeassistant;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.utils.Constants;

import java.util.List;
import java.util.Random;

/**
 * 调用python脚本的java类
 */
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

    /**
     * 弃用
     */
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

    /**
     * 在graph对象里以随机方式调用操作
     */
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

    /**
     * 初始化过程
     */
    public static OuterGraph init(String deviceName) {
        Constants.DEVICE_NAME = deviceName;
        return new OuterGraph(deviceName);
    }
}
