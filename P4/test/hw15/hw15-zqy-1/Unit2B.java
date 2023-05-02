import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UmlStateChartApi;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;

public class Unit2B implements UmlStateChartApi {
    private final HashMap<String, MyMach> machs = new HashMap<>();
    private final HashMap<String, MyRegion> regions = new HashMap<>();
    private final HashMap<String, MyState> states = new HashMap<>();
    private final HashMap<String, MyTrans> transs = new HashMap<>();
    private final HashMap<String, UmlEvent> uevts = new HashMap<>();

    public Unit2B(UmlElement[] elements) {
        for (UmlElement e : elements) {
            String eid = e.getId();
            switch (e.getElementType()) {
                // level 0
                case UML_STATE_MACHINE:
                    machs.put(eid, new MyMach((UmlStateMachine) e));
                    break;
                // level 1
                case UML_REGION:
                    machs.get(e.getParentId()).getRegions().add(eid);
                    regions.put(eid, new MyRegion((UmlRegion) e));
                    break;
                // level 2
                case UML_STATE:
                case UML_FINAL_STATE:
                case UML_PSEUDOSTATE: {
                    MyRegion reg = regions.get(e.getParentId());
                    reg.getStates().add(eid);
                    MyMach mach = machs.get(reg.getUreg().getParentId());
                    mach.getStates().add(eid);
                    states.put(eid, new MyState(e));
                    if (e.getElementType() == ElementType.UML_PSEUDOSTATE) {
                        // assume exactly 1 initial state
                        mach.setInitialState(eid);
                    }
                    if (e.getElementType() == ElementType.UML_FINAL_STATE) {
                        mach.setHaveFinalState(true);
                    }
                    break;
                }
                // level 3
                case UML_TRANSITION: {
                    UmlTransition utrans = (UmlTransition) e;
                    states.get(utrans.getSource()).getSrcTTs().add(eid);
                    states.get(utrans.getTarget()).getTgtTTs().add(eid);
                    transs.put(eid, new MyTrans(utrans));
                    break;
                }
                // level 4
                case UML_EVENT:
                    transs.get(e.getParentId()).getEvents().add(eid);
                    uevts.put(eid, (UmlEvent) e);
                    break;
                case UML_OPAQUE_BEHAVIOR:
                    transs.get(e.getParentId()).getBehavs().add(eid);
                    break;
                default:
                    break;
            }
        }
    }

    public void checkForRule008() throws UmlRule008Exception {
        for (MyTrans t : transs.values()) {
            if (states.get(t.getUtrans().getSource()).getUele()
                    .getElementType() == ElementType.UML_FINAL_STATE) {
                throw new UmlRule008Exception();
            }
        }
    }

    public void checkForRule009() throws UmlRule009Exception {
        for (MyState st : states.values()) {
            ArrayList<UmlEvent> allEvents = new ArrayList<>();
            // add all events of all src transitions
            st.getSrcTTs().forEach(ttId -> {
                for (String evtId : transs.get(ttId).getEvents()) {
                    allEvents.add(uevts.get(evtId));
                }
            });
            while (allEvents.size() > 0) {
                UmlEvent first = allEvents.remove(0);
                ArrayList<UmlEvent> sameEvents = new ArrayList<>();
                sameEvents.add(first);
                allEvents.removeIf(evt -> {
                    if (evt.getName().equals(first.getName())) {
                        sameEvents.add(evt);
                        return true;
                    }
                    return false;
                });
                HashSet<String> guards = new HashSet<>();
                if (sameEvents.size() < 2) {
                    continue;
                }
                for (UmlEvent evt : sameEvents) {
                    String guard = transs.get(evt.getParentId()).getUtrans().getGuard();
                    if (guard == null
                            || !guards.add(transs.get(evt.getParentId()).getUtrans().getGuard())) {
                        throw new UmlRule009Exception();
                    }
                }
            }
        }
    }

    private MyMach findMach(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyMach result = null;
        for (MyMach mach : machs.values()) {
            if (stateMachineName.equals(mach.getUsm().getName())) {
                if (result != null) {
                    throw new StateMachineDuplicatedException(stateMachineName);
                }
                result = mach;
            }
        }
        if (result == null) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        return result;
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return findMach(stateMachineName).getStates().size();
    }

    private Pair<MyMach, MyState> findState(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyMach mach = findMach(stateMachineName);
        MyState result = null;
        for (String stateId : mach.getStates()) {
            MyState state = states.get(stateId);
            if (stateName.equals(state.getUele().getName())) {
                if (result != null) {
                    throw new StateDuplicatedException(stateMachineName, stateName);
                }
                result = state;
            }
        }
        if (result == null) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        return new Pair<>(mach, result);
    }

    // exceptId can be null
    private boolean canReachAFinal(MyMach mach, String exceptId) {
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> queue = new LinkedList<>();
        // bfs on graph
        queue.add(mach.getInitialState());
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            if (visited.add(currentId) == false) {
                continue;
            }
            // not visited current, added to visited
            if (currentId.equals(exceptId)) {
                continue;
            }
            MyState state = states.get(currentId);
            if (state.getUele().getElementType() == ElementType.UML_FINAL_STATE) {
                // we reached a final state
                return true;
            }
            for (String ttId : state.getSrcTTs()) {
                MyTrans trans = transs.get(ttId);
                String targetId = trans.getUtrans().getTarget();
                queue.add(targetId);
            }
        }
        // we never reached a final state
        return false;
    }

    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        Pair<MyMach, MyState> pair = findState(stateMachineName, stateName);
        MyMach mach = pair.getFirst();
        MyState state = pair.getSecond();
        // assume state is not initial or final state.
        if (mach.isHaveFinalState() == false) {
            // short: no final state
            return false;
        }
        if (mach.getCanReachFinal() == null) {
            mach.setCanReachFinal(canReachAFinal(mach, null));
        }
        if (mach.getCanReachFinal() == true) {
            return !canReachAFinal(mach, state.getUele().getId());
        }
        return false;
    }

    public List<String> getTransitionTrigger(String stateMachineName, String sourceStateName,
            String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        Pair<MyMach, MyState> pair1 = findState(stateMachineName, sourceStateName);
        Pair<MyMach, MyState> pair2 = findState(stateMachineName, targetStateName);
        MyState srcState = pair1.getSecond();
        MyState tgtState = pair2.getSecond();
        HashSet<String> eventIds = new HashSet<>();
        String tgtId = tgtState.getUele().getId();
        for (String ttId : srcState.getSrcTTs()) {
            MyTrans tt = transs.get(ttId);
            if (tgtId.equals(tt.getUtrans().getTarget())) {
                eventIds.addAll(tt.getEvents());
            }
        }
        if (eventIds.size() == 0) {
            throw new TransitionNotFoundException(stateMachineName, sourceStateName,
                    targetStateName);
        }
        return eventIds.stream()//
                .map(evtId -> uevts.get(evtId).getName()) // event id to name
                .collect(Collectors.toList()); // collect
    }
}
