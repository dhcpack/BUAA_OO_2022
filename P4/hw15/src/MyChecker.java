import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class MyChecker {
    public void checkForUml003(HashMap<String, MyInterfaceClass> id2interfaceClass)
            throws UmlRule003Exception {
        HashSet<UmlClassOrInterface> res = new HashSet<>();
        for (Map.Entry<String, MyInterfaceClass> map : id2interfaceClass.entrySet()) {
            HashSet<String> visited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(map.getKey());
            while (!queue.isEmpty()) {
                String currId = queue.remove();
                MyInterfaceClass currInterfaceClass = id2interfaceClass.get(currId);
                if (visited.contains(currId)) {
                    if (currId.equals(map.getKey())) {
                        res.add((UmlClassOrInterface) map.getValue().getUmlElement());
                        break;
                    }
                } else {
                    visited.add(currId);
                    queue.addAll(currInterfaceClass.getChildren().stream()
                            .map(MyInterfaceClass::getId).collect(Collectors.toList()));
                }
            }
        }
        if (!res.isEmpty()) {
            throw new UmlRule003Exception(res);
        }
    }

    public void checkForUml004(HashMap<String, MyInterfaceClass> id2interfaceClass)
            throws UmlRule004Exception {  // 只检查generation吧
        HashSet<UmlClassOrInterface> res = new HashSet<>();
        for (Map.Entry<String, MyInterfaceClass> map : id2interfaceClass.entrySet()) {
            HashSet<String> inherited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(map.getKey());
            while (!queue.isEmpty()) {
                String currId = queue.remove();
                MyInterfaceClass currInterfaceClass = id2interfaceClass.get(currId);
                if (inherited.contains(currId)) {
                    res.add((UmlClassOrInterface) map.getValue().getUmlElement());
                    break;
                }
                inherited.add(currId);
                queue.addAll(currInterfaceClass.getParents().stream().map(MyInterfaceClass::getId)
                        .collect(Collectors.toList()));
            }
        }
        if (!res.isEmpty()) {
            throw new UmlRule004Exception(res);
        }
    }

    private boolean isEmptyName(String name) {
        if (name != null) {
            char[] chars = name.toCharArray();
            for (char c : chars) {
                if (c != ' ' && c != '\t') {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkForUml009(ArrayList<MyState> states, HashMap<String, Object> id2elm)
            throws UmlRule009Exception {
        for (MyState state : states) {
            HashMap<String, HashSet<UmlEvent>> triggers = new HashMap<>();
            for (MyTransition transition : state.getTransitions()) {
                for (UmlEvent event : transition.getEvents()) {
                    if (!triggers.containsKey(event.getName())) {
                        triggers.put(event.getName(), new HashSet<>());
                    }
                    triggers.get(event.getName()).add(event);
                }
            }
            for (HashSet<UmlEvent> events : triggers.values()) {  // 同名的event
                if (events.size() < 2) {
                    continue;
                }
                HashSet<String> guards = new HashSet<>();
                for (UmlEvent event : events) {
                    String guard = ((MyTransition) id2elm.get(event.getParentId())).getGuard();
                    if (isEmptyName(guard) || guards.contains(guard)) {  // 看同名event中是否有相同的guard
                        throw new UmlRule009Exception();
                    }
                    guards.add(guard);
                }
            }
        }

    }
}
