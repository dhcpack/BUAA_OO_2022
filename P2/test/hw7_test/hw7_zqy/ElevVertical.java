import java.util.ArrayList;

public class ElevVertical extends Thread {
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

    private final int elevId;
    private final char building;
    private final long timeFloor;
    private final int capacity;

    private final BuildingQueues allQueues;
    private final MyQueue<MyPerson> queue;

    public ElevVertical(int id, char building, double speed, int capacity, int floor,
            BuildingQueues allQueues) {
        this.elevId = id;
        this.building = building;
        this.timeFloor = (long) (speed * 1000);
        this.capacity = capacity;
        this.floor = floor;
        this.allQueues = allQueues;
        this.queue = allQueues.getVertQueues().get(building);
    }

    private int floor;
    private final ArrayList<MyPerson> people = new ArrayList<>();

    public ArrayList<MyPerson> tryDropOff() {
        // try remove people that can be dropped off, from people
        ArrayList<MyPerson> dropped = new ArrayList<>();
        people.removeIf(ppl -> {
            if (ppl.getNextFloor() != floor) {
                return false;
            }
            dropped.add(ppl);
            return true;
        });
        return dropped;
    }

    public ArrayList<MyPerson> tryTakeOn(Cmp moveDir) {
        // try add people and remove them from the queue
        ArrayList<MyPerson> taken = new ArrayList<>();
        queue.syncRemoveIf(ppl -> {
            // same floor
            if (ppl.getCurrFloor() != floor) {
                return false;
            }
            // moving, same direction
            if (moveDir != Cmp.EQ && moveDir != Cmp.cmp(ppl.getCurrFloor(), ppl.getNextFloor())) {
                return false;
            }
            // have capacity
            if (people.size() >= capacity) {
                return false;
            }
            people.add(ppl);
            taken.add(ppl);
            return true;
        });
        return taken;
    }

    private final static long TIME_DOOR = 400;

    public void atFloor(Cmp moveDir) {
        ArrayList<MyPerson> drop = tryDropOff();
        ArrayList<MyPerson> took = tryTakeOn(moveDir);
        if (drop.size() + took.size() > 0) {
            MyOutput.printOpen(building, floor, elevId);
            // open and close door
            try {
                Thread.sleep(TIME_DOOR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            drop.addAll(tryDropOff());
            took.addAll(tryTakeOn(moveDir));
            for (MyPerson pd : drop) {
                MyOutput.printOut(pd.getPersonId(), building, floor, elevId);
                allQueues.personNext(pd);
            }
            for (MyPerson pt : took) {
                MyOutput.printIn(pt.getPersonId(), building, floor, elevId);
            }
            MyOutput.printClose(building, floor, elevId);
        }
    }

    public void tryMoveTo(int targetFloor) {
        int beginFloor = floor;
        Cmp moveDir = Cmp.cmp(beginFloor, targetFloor);
        for (int fl : Util.intIncl(beginFloor, targetFloor)) {
            floor = fl;
            if (floor != beginFloor) {
                MyOutput.printArrive(building, floor, elevId);
            }
            atFloor(moveDir);
            if (floor != targetFloor) {
                // move between floors
                try {
                    Thread.sleep(timeFloor);
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
                        MyPerson firstReq = queue.tryGetFirst();
                        if (firstReq == null) {
                            break;
                        }
                        target = firstReq.getCurrFloor();
                    } else {
                        target = people.get(0).getNextFloor();
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
