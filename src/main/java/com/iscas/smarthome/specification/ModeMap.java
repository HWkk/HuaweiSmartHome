package com.iscas.smarthome.specification;

import com.iscas.smarthome.stateautomaton.state.OuterState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModeMap {

    private static HashMap<String, OuterState> map = new HashMap<>();

    public static void addState(String mode, String deviceName) {
        if(!map.containsKey(mode))
            map.put(mode, new OuterState(mode, deviceName));
    }

    public static boolean containsState(String mode) {
        return map.containsKey(mode);
    }

    public static OuterState getState(String mode) {
        return map.get(mode);
    }

    public static List<OuterState> getAllStates() {
        List<OuterState> list = new ArrayList<>();
        for(OuterState state : map.values())
            list.add(state);
        return list;
    }

    public static HashMap<String, OuterState> getMap() {
        return map;
    }

    public static void setMap(HashMap<String, OuterState> m) {
        map = m;
    }
}
