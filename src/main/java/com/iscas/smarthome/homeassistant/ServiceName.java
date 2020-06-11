package com.iscas.smarthome.homeassistant;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设备与对应的操作名称
 */
public class ServiceName {

    public static String preService = "";

    //key为entity_id，value为调用的服务名
    private static HashMap<String, ArrayList<String>> serviceName;

    public static void init() {
        serviceName = new HashMap<>();

        //空气净化器
        serviceName.put(EntityName.AIR_PURIFIER_NAME, new ArrayList<String>());
        serviceName.get(EntityName.AIR_PURIFIER_NAME).add("Auto");
        serviceName.get(EntityName.AIR_PURIFIER_NAME).add("Silent");
        serviceName.get(EntityName.AIR_PURIFIER_NAME).add("Favorite");

        //加湿器
        serviceName.put(EntityName.AIR_HUMIDIFIER_NAME, new ArrayList<String>());
        serviceName.get(EntityName.AIR_HUMIDIFIER_NAME).add("Silent");
        serviceName.get(EntityName.AIR_HUMIDIFIER_NAME).add("Medium");
        serviceName.get(EntityName.AIR_HUMIDIFIER_NAME).add("High");
    }

    public static ArrayList<String> getServices(String entityId) {
        return serviceName.get(entityId);
    }

    public static void filter(String entityName) {

    }
}
