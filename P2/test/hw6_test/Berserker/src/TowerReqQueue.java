import java.util.ArrayList;

public class TowerReqQueue {
    private static final int MAX_FLOOR = 10;
    private final ArrayList<ArrayList<MyRequest>> upReqs;
    private final ArrayList<ArrayList<MyRequest>> downReqs;
    private boolean end;
    private final char tower;
    private int size;

    public TowerReqQueue(char tower) {
        upReqs = new ArrayList<>(MAX_FLOOR + 1);
        downReqs = new ArrayList<>(MAX_FLOOR + 1);
        for (int i = 0; i < MAX_FLOOR; i++) {
            upReqs.add(new ArrayList<>());
            downReqs.add(new ArrayList<>());
        }
        this.tower = tower;
        size = 0;
        end = false;
    }

    public synchronized void putReq(MyRequest req) {
        if (req.isUp()) {
            upReqs.get(req.getFromFloor() - 1).add(req);
        } else {
            downReqs.get(req.getFromFloor() - 1).add(req);
        }
        size++;
        notifyAll();
    }

    public synchronized MyRequest takeReq(int floor, boolean up) {
        while (isEmpty() && !end) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (end && size == 0) {
            return null;
        }
        size--;
        if (up) {
            return upReqs.get(floor - 1).remove(0);
        } else {
            return downReqs.get(floor - 1).remove(0);
        }
    }

    public synchronized boolean haveReq(int floor, boolean up) {
        if (up) {
            return !upReqs.get(floor - 1).isEmpty();
        } else {
            return !downReqs.get(floor - 1).isEmpty();
        }
    }

    public synchronized boolean haveReq(int floor) {
        return haveReq(floor, true) || haveReq(floor, false);
    }

    public synchronized boolean notHaveDirReq(int floor, boolean dirUp) {
        if (dirUp) {
            for (int i = floor + 1; i <= MAX_FLOOR; i++) {
                if (haveReq(i)) {
                    return false;
                }
            }
        } else {
            for (int i = floor - 1; i >= 1; i--) {
                if (haveReq(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized ArrayList<MyRequest> getReqs(int floor, int elevSize, boolean dirUp) {
        ArrayList<MyRequest> temp = new ArrayList<>();
        int count = elevSize;
        if (!haveReq(floor)) {
            return temp;
        }
        if (elevSize == 0) {
            if (notHaveDirReq(floor, dirUp) && !haveReq(floor, dirUp)) {
                if (dirUp) {
                    while (haveReq(floor, false) && count < 6) {
                        temp.add(takeReq(floor, false));
                        count++;
                    }
                } else {
                    while (haveReq(floor, true) && count < 6) {
                        temp.add(takeReq(floor, true));
                        count++;
                    }
                }
                return temp;
            }
        }
        if (dirUp) {
            while (haveReq(floor, true) && count < 6) {
                temp.add(takeReq(floor, true));
                count++;
            }
        } else {
            while (haveReq(floor, false) && count < 6) {
                temp.add(takeReq(floor, false));
                count++;
            }
        }
        return temp;
    }

    public int size() {
        return size;
    }

    public synchronized void end() {
        end = true;
        notifyAll();
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        return "<TowerQueue in tower: " + tower + ">";
    }
}
