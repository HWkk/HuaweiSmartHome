package com.iscas.smarthome.homeassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttributesName {

    //key为entity_id, value为mode属性在HA中的key值
    private static HashMap<String, String> modeAttribute = new HashMap<>();

    //key为entity_id，value为要获取的属性名
    private static HashMap<String, List<String>> attributesName = new HashMap<>();

    public static void init() {
        //空气净化器
        modeAttribute.put(EntityName.AIR_PURIFIER_NAME, "mode");
        attributesName.put(EntityName.AIR_PURIFIER_NAME, new ArrayList<String>());

        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("temperature");
        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("humidity");
        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("aqi");
        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("motor_speed");
        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("average_aqi");
        attributesName.get(EntityName.AIR_PURIFIER_NAME).add("purify_volume");


        //加湿器
        modeAttribute.put(EntityName.AIR_HUMIDIFIER_NAME, "mode");
        attributesName.put(EntityName.AIR_HUMIDIFIER_NAME, new ArrayList<String>());

        attributesName.get(EntityName.AIR_HUMIDIFIER_NAME).add("temperature");
        attributesName.get(EntityName.AIR_HUMIDIFIER_NAME).add("target_humidity");
        attributesName.get(EntityName.AIR_HUMIDIFIER_NAME).add("humidity");
    }

    public static String getModeName(String entityId) {
        return modeAttribute.get(entityId);
    }

    public static List<String> getAttributes(String entityId) {
        return attributesName.get(entityId);
    }

    public static void addEntity(String entityName, List<String> attributes) {
        modeAttribute.put(entityName, "mode");
        attributesName.put(entityName, attributes);
    }
}
