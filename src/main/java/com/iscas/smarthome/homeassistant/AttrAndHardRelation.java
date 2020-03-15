package com.iscas.smarthome.homeassistant;

import java.util.HashMap;

public class AttrAndHardRelation {

    private static HashMap<String, String> relation = new HashMap<>();

    public static void put(String attributeName, String hardwareName) {
        relation.put(attributeName, hardwareName);
    }

    public static String getHardwareName(String attributeName) {
        return relation.get(attributeName);
    }

    public static String print() {
        return relation.toString();
    }

    public static boolean containsAttribute(String attributeName) {
        return relation.containsKey(attributeName);
    }
}
