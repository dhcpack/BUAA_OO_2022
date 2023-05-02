import java.util.ArrayList;
import java.util.function.Predicate;
import com.oocourse.elevator2.PersonRequest;

public class ElevHorizontal extends Thread {
    // information of the elevator
    private final int elevId;
    private final int floor;

    public ElevHorizontal(int id, int floor, char building, MyQueue<PersonRequest> queue) {
        super(String.format("<elev-h-%d-%d>", id, floor));
        this.elevId = id;
        this.floor = floor;
        this.queue = queue;
        this.building = building;
    }

    private final MyQueue<PersonRequest> queue;

    // limit constants
    private static final int MAX_PEOPLE = 6;

    private char building;
    private final ArrayList<PersonRequest> people = new ArrayList<>(MAX_PEOPLE);

    //
    // methods
    //

    public ArrayList<PersonRequest> tryDropOff() {
        ArrayList<PersonRequest> dropped = new ArrayList<>();
        people.removeIf(new Predicate<PersonRequest>() {
            public boolean test(PersonRequest ppl) {
                // same building
                if (ppl.getToBuilding() != building) {
                    return false;
                }
                dropped.add(ppl);
                return true;
            }
        });
        return dropped;
    }

    public ArrayList<PersonRequest> tryTakeOn() {
        // try add people and remove them from the queue
        ArrayList<PersonRequest> taken = new ArrayList<>();
        queue.syncRemoveIf(new Predicate<PersonRequest>() {
            public boolean test(PersonRequest req) {
                // same building
                if (req.getFromBuilding() != building) {
                    return false;
                }
                // have capacity
                if (people.size() >= MAX_PEOPLE) {
                    return false;
                }
                people.add(req);
                taken.add(req);
                return true;
            }
        });
        return taken;
    }

    private static final long TIME_DOOR = 400;

    public void atBuilding() {
        ArrayList<PersonRequest> drop = tryDropOff();
        ArrayList<PersonRequest> took = tryTakeOn();
        if (drop.size() + took.size() > 0) {
            MyOutput.printOpen(building, floor, elevId);
            for (PersonRequest pd : drop) {
                MyOutput.printOut(pd.getPersonId(), building, floor, elevId);
            }
            for (PersonRequest pt : took) {
                MyOutput.printIn(pt.getPersonId(), building, floor, elevId);
            }
            // open and close door
            try {
                Thread.sleep(TIME_DOOR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            MyOutput.printClose(building, floor, elevId);
        }
    }

    private final static long TIME_BUILDING = 200;

    public void tryMoveTo(char targetBuilding) {
        char beginBuilding = building;
        for (char bl : Util.buildingIncl(beginBuilding, targetBuilding)) {
            building = bl;
            if (building != beginBuilding) {
                MyOutput.printArrive(building, floor, elevId);
            }
            atBuilding();
            if (building != targetBuilding) {
                // move between buildings
                try {
                    Thread.sleep(TIME_BUILDING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        try {
            while (queue.waitForPut()) {
                while (true) {
                    // ALS: find the main request, and get the target we should move to
                    char target;
                    if (people.isEmpty()) {
                        PersonRequest firstReq = queue.tryGetFirst();
                        if (firstReq == null) {
                            break;
                        }
                        target = firstReq.getFromBuilding();
                    } else {
                        target = people.get(0).getToBuilding();
                    }
                    // ALS: move to the target
                    tryMoveTo(target);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
