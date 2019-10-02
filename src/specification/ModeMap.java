package specification;

import stateautomaton.state.OuterState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModeMap {

    private static HashMap<String, OuterState> map = new HashMap<>();

    public static void addState(String mode) {
        if(!map.containsKey(mode))
            map.put(mode, new OuterState(mode));
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
}
