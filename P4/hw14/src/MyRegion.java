import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MyRegion {
    private final UmlRegion umlRegion;
    private int states;
    private final HashMap<String, ArrayList<MyState>> name2state = new HashMap<>();
    private MyState startState;
    private final ArrayList<MyState> finalStates = new ArrayList<>();

    public MyRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }

    public void addState(MyState myState) {
        states++;
        if (!name2state.containsKey(myState.getName())) {
            name2state.put(myState.getName(), new ArrayList<>());
        }
        name2state.get(myState.getName()).add(myState);
        if (myState.getType() == ElementType.UML_PSEUDOSTATE) {
            this.startState = myState;
        } else if (myState.getType() == ElementType.UML_FINAL_STATE) {
            this.finalStates.add(myState);
        }
    }

    public boolean hasFinalState() {
        return this.finalStates.size() != 0;
    }

    public boolean isFinalState(MyState myState) {
        return this.finalStates.contains(myState);
    }

    public boolean isStartState(MyState myState) {
        return myState.equals(startState);
    }

    public MyState getStartState() {
        return startState;
    }

    // 状态图 1
    public int getStateCount() {
        return this.states;
    }

    private MyState findState(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        ArrayList<MyState> myStates = name2state.get(stateName);
        if (myStates == null) {
            throw new StateNotFoundException(stateMachineName, stateName);
        } else if (myStates.size() != 1) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        return myStates.get(0);
    }

    // 状态图 2
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        MyState deleteState = findState(stateMachineName, stateName);
        if (!hasFinalState() || isFinalState(deleteState) || isStartState(deleteState)) {
            return false;
        }
        MyState currentState = getStartState();
        HashSet<MyState> visited1 = new HashSet<>();
        visited1.add(currentState);
        if (!couldReachFinal(currentState, visited1, null)) {
            return false;
        }
        HashSet<MyState> visited2 = new HashSet<>();
        return !couldReachFinal(currentState, visited2, deleteState);
    }

    private boolean couldReachFinal(MyState currentState, HashSet<MyState> visited,
                                    MyState deleteState) {
        if (isFinalState(currentState)) {
            return true;
        }
        HashSet<MyState> nextStates = currentState.getTransitions().stream().
                map(MyTransition::getTargetState).collect(Collectors.toCollection(HashSet::new));
        for (MyState myState : nextStates) {
            if ((!visited.contains(myState)) && ((deleteState == null) || (!deleteState.equals(
                    myState)))) {
                visited.add(myState);
                if (couldReachFinal(myState, visited, deleteState)) {
                    return true;
                }
                visited.remove(myState);
            }
        }
        return false;
    }

    // 状态图 3
    public List<String> getTransitionTrigger(String stateMachineName, String sourceStateName,
                                             String targetStateName)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyState sourceState = findState(stateMachineName, sourceStateName);
        MyState targetState = findState(stateMachineName, targetStateName);
        String targetId = targetState.getId();
        HashSet<UmlEvent> events = new HashSet<>();
        boolean flag = false;
        for (MyTransition myTransition : sourceState.getTransitions()) {
            if (myTransition.getTargetId().equals(targetId)) {
                events.addAll(myTransition.getEvents());
                flag = true;
            }
        }
        if (!flag) {
            throw new TransitionNotFoundException(stateMachineName, sourceStateName,
                    targetStateName);
        }
        return events.stream().map(UmlEvent::getName).collect(Collectors.toList());
    }
}
