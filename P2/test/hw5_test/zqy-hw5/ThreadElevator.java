import java.util.ArrayList;
import java.util.function.Predicate;
import com.oocourse.elevator1.PersonRequest;

public class ThreadElevator extends Thread {
    private enum Cmp {
        LT, EQ, GT;

        public static <T extends Comparable<T>> Cmp cmp(T a, T b) {
            int c = a.compareTo(b);
            if (c == 0) {
                return EQ;
            }
            if (c > 0) {
                return GT;
            }
            return LT;
        }
    }


    // information of the elevator
    private final int elevId;
    private final char building;

    public ThreadElevator(int id, char building) {
        super(String.format("<elev-%d-%s>", id, building));
        this.elevId = id;
        this.building = building;
    }

    private final MyQueue<PersonRequest> queue = new MyQueue<>();

    public MyQueue<PersonRequest> getQueue() {
        return queue;
    }

    // limit constants
    private static final int MAX_PEOPLE = 6;
    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 10;

    // state of the elevator
    private int floor = 1;
    private final ArrayList<PersonRequest> people = new ArrayList<>(MAX_PEOPLE);

    //
    // methods
    //

    public ArrayList<PersonRequest> tryDropOff() {
        // try remove people that can be dropped off, from people
        ArrayList<PersonRequest> dropped = new ArrayList<>();
        people.removeIf(new Predicate<PersonRequest>() {
            public boolean test(PersonRequest ppl) {
                // same floor
                if (ppl.getToFloor() != floor) {
                    return false;
                }
                dropped.add(ppl);
                return true;
            }
        });
        return dropped;
    }

    public ArrayList<PersonRequest> tryTakeOn(Cmp moveDir) {
        // try add people and remove them from the queue
        ArrayList<PersonRequest> taken = new ArrayList<>();
        queue.syncRemoveIf(new Predicate<PersonRequest>() {
            public boolean test(PersonRequest req) {
                // same floor
                if (req.getFromFloor() != floor) {
                    return false;
                }
                // same direction
                if (moveDir != Cmp.EQ // elevator moving
                        && Cmp.cmp(req.getFromFloor(), req.getToFloor()) != moveDir) {
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

    public void atFloor(Cmp moveDir) {
        ArrayList<PersonRequest> drop = tryDropOff();
        ArrayList<PersonRequest> took = tryTakeOn(moveDir);
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
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            MyOutput.printClose(building, floor, elevId);
        }
    }

    public void tryMoveTo(int targetFloor) {
        int beginFloor = floor;
        Cmp moveDir = Cmp.cmp(floor, targetFloor);
        for (int fl : Util.inclRange(floor, targetFloor)) {
            floor = fl;
            if (floor != beginFloor) {
                MyOutput.printArrive(building, floor, elevId);
            }
            atFloor(moveDir);
            if (floor != targetFloor) {
                // move between floors
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //
            }
        }
    }

    public void run() {
        try {
            while (queue.waitForPut()) {
                while (true) {
                    // ALS: find the main request, and get the target we should move to
                    int target;
                    if (people.isEmpty()) {
                        PersonRequest firstReq = queue.tryTakeFirst();
                        if (firstReq == null) {
                            break;
                        }
                        target = firstReq.getFromFloor();
                    } else {
                        target = people.get(0).getToFloor();
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
