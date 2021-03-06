package com.iscas.smarthome.specification;

import com.iscas.smarthome.stateautomaton.state.OuterState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//弃用
public class OuterEvent implements Event {

    private static HashMap<String, OuterState> eventMap = new HashMap<>();

    public static void addEvent(String event) {
        if(!eventMap.containsKey(event))
            eventMap.put(event, new OuterState(event, ""));
    }

    public static boolean containsEvent(String event) {
        return eventMap.containsKey(event);
    }

    public static OuterState getState(String event) {
        return eventMap.get(event);
    }

    public static List<OuterState> getAllStates() {
        List<OuterState> list = new ArrayList<>();
        for(OuterState state : eventMap.values())
            list.add(state);
        return list;
    }
}
