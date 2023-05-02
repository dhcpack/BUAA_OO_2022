import java.util.ArrayList;

public class TowerElevator extends Thread {
    private static final int MAX_TOWER = 5;
    private static final int MAX_NUM = 6;
    private final int id;
    private final int floor;
    private int tower;
    private final FloorReqQueue reqQueue;
    private final ArrayList<ArrayList<MyRequest>> passengers;
    private ArrayList<MyRequest> buffer;
    private boolean dirClock;
    private int size;

    public TowerElevator(int id, int floor, FloorReqQueue reqQueue) {
        super(id + "");
        this.id = id;
        this.floor = floor;
        tower = 0;
        this.reqQueue = reqQueue;
        dirClock = true;
        passengers = new ArrayList<>(MAX_TOWER + 1);
        for (int i = 0; i < MAX_TOWER; i++) {
            passengers.add(new ArrayList<>(MAX_NUM + 1));
        }
        size = 0;
        buffer = null;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (reqQueue) {
                while (reqQueue.isEmpty() && isEmpty()) {
                    if (reqQueue.isEnd()) {
                        return;
                    }
                    try {
                        reqQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            buffer = reqQueue.getReqs(tower, size - passengers.get(tower).size());
            exchangePassenger();
            checkDir();
            move();
        }
    }

    private void move() {
        if (size == 0 && reqQueue.isEmpty()) {
            return;
        }
        if (dirClock) {
            clockMove();
        } else {
            antiClockMove();
        }
    }

    private void clockMove() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tower = nextTower(tower);
        SafeOutput.println("ARRIVE-" + getTower() + '-' + floor + '-' + id);
    }

    private void antiClockMove() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tower = prevTower(tower);
        SafeOutput.println("ARRIVE-" + getTower() + '-' + floor + '-' + id);
    }

    private void passengerIn() {
        if (buffer.isEmpty() && !reqQueue.haveReq(tower)) {
            return;
        }
        for (MyRequest req : buffer) {
            passengers.get(req.getToTower()).add(req);
            size++;
            SafeOutput.println("IN-" + req.getId() + '-' + getTower() + '-' + floor + '-' + id);
        }
        buffer = reqQueue.getReqs(tower, size - passengers.get(tower).size());
        for (MyRequest req : buffer) {
            passengers.get(req.getToTower()).add(req);
            size++;
            SafeOutput.println("IN-" + req.getId() + '-' + getTower() + '-' + floor + '-' + id);
        }
    }

    private void checkDir() {
        boolean judClock = true;
        boolean judAntiClock = false;
        for (ArrayList<MyRequest> arr : passengers) {
            for (MyRequest req : arr) {
                judClock &= isClock(req);
                judAntiClock |= isClock(req);
            }
        }
        if (judClock && notHaveAntiClockReq() && !dirClock) {
            dirClock = true;
        } else if (!judAntiClock && notHaveClockReq() && dirClock) {
            dirClock = false;
        }
    }

    private void passengerOut() {
        if (isEmpty()) {
            return;
        }
        for (MyRequest req : passengers.get(tower)) {
            SafeOutput.println("OUT-" + req.getId() + '-' + getTower() + '-' + floor + '-' + id);
        }
        size -= passengers.get(tower).size();
        passengers.get(tower).clear();
    }

    private void exchangePassenger() {
        if (needExchange()) {
            SafeOutput.println("OPEN-" + getTower() + '-' + floor + '-' + id);
            passengerOut();
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passengerIn();
            SafeOutput.println("CLOSE-" + getTower() + '-' + floor + '-' + id);
        }
    }

    private boolean needExchange() {
        if (!buffer.isEmpty()) {
            return true;
        }
        return !passengers.get(tower).isEmpty();
    }

    private boolean notHaveClockReq() {
        int t1 = nextTower(tower);
        int t2 = nextTower(t1);
        return !reqQueue.haveReq(t1) && !reqQueue.haveReq(t2);
    }

    private boolean notHaveAntiClockReq() {
        int t1 = prevTower(tower);
        int t2 = prevTower(t1);
        return !reqQueue.haveReq(t1) && !reqQueue.haveReq(t2);
    }

    private int nextTower(int t) {
        if (t == 4) {
            return 0;
        } else {
            return t + 1;
        }
    }

    private int prevTower(int t) {
        if (t == 0) {
            return 4;
        } else {
            return t - 1;
        }
    }

    private boolean isClock(MyRequest req) {
        int temp = req.getToTower() - tower;
        if (temp < 0) {
            temp += 5;
        }
        return temp <= 2;
    }

    private char getTower() {
        return (char) ('A' + tower);
    }

    private boolean isEmpty() {
        return size == 0;
    }
}
