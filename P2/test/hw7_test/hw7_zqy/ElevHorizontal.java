import java.util.ArrayList;

public class ElevHorizontal extends Thread {
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

    private static Cmp cmpBuilding(char begIncl, char endIncl) {
        int a = begIncl - 'A';
        int b = endIncl - 'A';
        int x = Math.abs(a - b);
        int y = Util.BUILDINGS.length - x;
        if (y < x) {
            if (a < b) {
                a += Util.BUILDINGS.length;
            } else {
                b += Util.BUILDINGS.length;
            }
        }
        return Cmp.cmp(a, b);
    }

    private final int elevId;
    private final int floor;
    private final long timeBuilding;
    private final int capacity;

    private final BuildingQueues allQueues;
    private final MyQueue<MyPerson> queue;
    private final SwitchInfo switchInfo;

    public ElevHorizontal(int id, int floor, double speed, int capacity, char building,
            BuildingQueues allQueues, SwitchInfo switchInfo) {
        this.elevId = id;
        this.floor = floor;
        this.timeBuilding = (long) (speed * 1000);
        this.capacity = capacity;
        this.building = building;
        this.allQueues = allQueues;
        this.queue = allQueues.getHoriQueues().get(floor);
        this.switchInfo = switchInfo;
    }

    private char building;
    private final ArrayList<MyPerson> people = new ArrayList<>();

    public ArrayList<MyPerson> tryDropOff() {
        ArrayList<MyPerson> dropped = new ArrayList<>();
        people.removeIf(ppl -> {
            if (ppl.getNextBuilding() != building) {
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
            // same building
            if (ppl.getCurrBuilding() != building) {
                return false;
            }
            // can open door
            if (!(switchInfo.canOpenAt(ppl.getCurrBuilding())
                    && switchInfo.canOpenAt(ppl.getNextBuilding()))) {
                return false;
            }
            // moving, same dir
            // REMINDER: might break things
            if (moveDir != Cmp.EQ
                    && moveDir != cmpBuilding(ppl.getCurrBuilding(), ppl.getNextBuilding())) {
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

    public void atBuilding(Cmp moveDir) {
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

    public void tryMoveTo(char targetBuilding) {
        char beginBuilding = building;
        Cmp moveDir = cmpBuilding(beginBuilding, targetBuilding);
        for (char bl : Util.buildingIncl(beginBuilding, targetBuilding)) {
            building = bl;
            if (building != beginBuilding) {
                MyOutput.printArrive(building, floor, elevId);
            }
            atBuilding(moveDir);
            if (building != targetBuilding) {
                // move between buildings
                try {
                    Thread.sleep(timeBuilding);
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
                        MyPerson[] firstReq = {null};
                        queue.syncForEach(ppl -> {
                            if (switchInfo.canOpenAt(ppl.getCurrBuilding())
                                    && switchInfo.canOpenAt(ppl.getNextBuilding())) {
                                firstReq[0] = ppl;
                            }
                        });
                        if (firstReq[0] == null) {
                            queue.waitForNextPut();
                            break;
                        }
                        target = firstReq[0].getCurrBuilding();
                    } else {
                        target = people.get(0).getNextBuilding();
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
