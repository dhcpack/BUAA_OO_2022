import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ElevatorSystem extends Thread {
    private static final ElevatorSystem INSTANCE = new ElevatorSystem();

    private ElevatorSystem() {
    }

    public void initial() {
        alist.add(Default.defaultVerElevator(1, 'A'));
        blist.add(Default.defaultVerElevator(2, 'B'));
        clist.add(Default.defaultVerElevator(3, 'C'));
        dlist.add(Default.defaultVerElevator(4, 'D'));
        elist.add(Default.defaultVerElevator(5, 'E'));
    }

    public static ElevatorSystem getInstance() {
        return INSTANCE;
    }

    private final ElevatorQueue alist = new ElevatorQueue(new ArrayList<>());

    private final ElevatorQueue blist = new ElevatorQueue(new ArrayList<>());

    private final ElevatorQueue clist = new ElevatorQueue(new ArrayList<>());

    private final ElevatorQueue dlist = new ElevatorQueue(new ArrayList<>());

    private final ElevatorQueue elist = new ElevatorQueue(new ArrayList<>());

    private final HashMap<Integer, ElevatorQueue> horizontalEles = new HashMap<>();

    public ElevatorQueue getEleByBuilding(char building) {
        switch (building) {
            case 'A':
                return alist;
            case 'B':
                return blist;
            case 'C':
                return clist;
            case 'D':
                return dlist;
            case 'E':
                return elist;
            default:
                return null;
        }
    }

    public ElevatorQueue getEleByFloor(int floor) {
        return horizontalEles.get(floor);
    }

    public void notifyEle() {
        alist.notifyElevator();
        blist.notifyElevator();
        clist.notifyElevator();
        dlist.notifyElevator();
        elist.notifyElevator();
        for (ElevatorQueue eq : horizontalEles.values()) {
            eq.notifyElevator();
        }
    }

    public Elevator dispatch(PersonRequest p) {
        if (p.getFromFloor() != p.getToFloor()) {
            char building = p.getFromBuilding();
            return getEleByBuilding(building).dispatch(p);
        } else {
            int floor = p.getFromFloor();
            return getEleByFloor(floor).dispatch(p);
        }
    }

    public void addHorizontal(HoriElevator h, int floor) {
        if (horizontalEles.containsKey(floor)) {
            horizontalEles.get(floor).add(h);
        } else {
            ElevatorQueue eq = new ElevatorQueue(new ArrayList<>());
            eq.add(h);
            horizontalEles.put(floor, eq);
        }

    }

    public void addVertical(VerElevator v, char building) {
        getEleByBuilding(building).add(v);
    }

    @Override
    public void run() {
        initial();
    }
}
