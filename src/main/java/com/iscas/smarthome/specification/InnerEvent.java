package com.iscas.smarthome.specification;

import java.util.HashSet;

//弃用
public class InnerEvent {

    private static HashSet<String> events = new HashSet<>();

    public static boolean containsEvent(String event) {
        return events.contains(event);
    }

    public static void addEvent(String event) {
        events.add(event);
    }
}
