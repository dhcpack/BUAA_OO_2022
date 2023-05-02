import java.util.ArrayList;
import java.util.HashMap;

public class BuildingWaitQueue {
    private final HashMap<Integer, ArrayList<Passenger>> passengers = new HashMap<>();
    private char building;
    private boolean isEnd = false;
    private boolean debug = false;

    public BuildingWaitQueue(char building) {
        this.building = building;
        for (int i = 1; i <= 10; i++) {
            passengers.put(i, new ArrayList<>());
        }
    }

    public synchronized void addPassenger(ArrayList<Passenger> toAdd) {
        for (Passenger passenger : toAdd) {
            int from = passenger.getFromFloor();
            passengers.get(from).add(passenger);
            if (debug) {
                System.out.println("building " + building + " add " + passenger);
            }

        }
        if (toAdd.size() > 0) {
            this.notifyAll();
        }
    }

    public synchronized void setEnd() {
        this.isEnd = true;
        this.notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isNull() {
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += passengers.get(i).size();
        }
        return sum == 0;
    }

    public synchronized int getNearFloor(int nowFloor) {
        int min = 11;
        int target = 0;
        for (int i = 1; i <= 10; i++) {
            if (passengers.get(i).size() > 0) {
                if (Math.abs(i - nowFloor) < min) {
                    min = Math.abs(i - nowFloor);
                    target = i;
                }
            }
        }
        if (debug && target == 0) {
            System.out.println(building + " get Dir Wrong");
        }
        return target;
    }

    public synchronized boolean hasPassenger(int dir, int nowFloor) {
        for (Passenger passenger : passengers.get(nowFloor)) {
            if (passenger.getDir() == dir) {
                return true;
            }
        }
        return needChangeDir(nowFloor, dir);
    }

    public synchronized boolean needChangeDir(int nowFloor, int dir) {
        if (isNull()) {
            return false;
        }
        if (dir == 1 || dir == -1) {
            for (int i = nowFloor + dir; i <= 10 && i > 0; i += dir) {
                if (passengers.get(i).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized ArrayList<Passenger> getPassenger(int dir, int nowFloor, int size) {
        ArrayList<Passenger> ans = new ArrayList<>();
        int num1 = 0;
        int num2 = 0;
        int standard = dir;
        if (dir == 0) {
            for (Passenger passenger : passengers.get(nowFloor)) {
                if (passenger.getDir() == 1) {
                    num1++;
                } else {
                    num2++;
                }
            }
            if (num1 >= num2) {
                standard = 1;
            } else {
                standard = -1;
            }
        }
        for (Passenger passenger : passengers.get(nowFloor)) {
            if (passenger.getDir() == standard) {
                ans.add(passenger);
            }
            if (ans.size() >= size) {
                break;
            }
        }
        if (needChangeDir(nowFloor, dir) && ans.size() == 0) {
            for (Passenger passenger : passengers.get(nowFloor)) {
                ans.add(passenger);
                if (ans.size() >= size) {
                    break;
                }
            }
        }
        for (Passenger passenger : ans) {
            passengers.get(nowFloor).remove(passenger);
        }
        return ans;
    }

    public synchronized boolean needEnd() {
        return isEnd && isNull();
    }

    public synchronized boolean needWait() {
        if (isNull()) {
            try {
                wait();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
