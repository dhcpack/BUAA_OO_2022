import java.util.ArrayList;

public class FloorElevator extends Thread {
    private static final int MAX_FLOOR = 10;
    private static final int MAX_NUM = 6;
    private final int id;
    private final char tower;
    private int floor;
    private final TowerReqQueue reqQueue;
    private final ArrayList<ArrayList<MyRequest>> passengers;
    private ArrayList<MyRequest> buffer;
    private boolean dirUp;
    private int size;

    public FloorElevator(int id, char tower, TowerReqQueue reqQueue) {
        super(id + "");
        this.id = id;
        this.tower = tower;
        floor = 1;
        this.reqQueue = reqQueue;
        dirUp = true;
        passengers = new ArrayList<>(MAX_FLOOR + 1);
        for (int i = 0; i < MAX_FLOOR; i++) {
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
            buffer = reqQueue.getReqs(floor, size - passengers.get(floor - 1).size(), dirUp);
            exchangePassenger();
            checkDir();
            move();
        }
    }

    private void move() {
        if (size == 0 && reqQueue.isEmpty()) {
            return;
        }
        if (dirUp) {
            up();
        } else {
            down();
        }
        if (floor == 1) {
            dirUp = true;
        } else if (floor == MAX_FLOOR) {
            dirUp = false;
        }
    }

    private void up() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor++;
        SafeOutput.println("ARRIVE-" + tower + '-' + floor + '-' + id);
    }

    private void down() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor--;
        SafeOutput.println("ARRIVE-" + tower + '-' + floor + '-' + id);
    }

    private void passengerIn() {
        if (buffer.isEmpty() && !reqQueue.haveReq(floor)) {
            return;
        }
        for (MyRequest req : buffer) {
            passengers.get(req.getToFloor() - 1).add(req);
            size++;
            SafeOutput.println("IN-" + req.getId() + '-' + tower + '-' + floor + '-' + id);
        }
        buffer = reqQueue.getReqs(floor, size - passengers.get(floor - 1).size(), dirUp);
        for (MyRequest req : buffer) {
            passengers.get(req.getToFloor() - 1).add(req);
            size++;
            SafeOutput.println("IN-" + req.getId() + '-' + tower + '-' + floor + '-' + id);
        }
    }

    private void checkDir() {
        boolean judUp = true;
        boolean judDown = false;
        for (ArrayList<MyRequest> arr : passengers) {
            for (MyRequest req : arr) {
                judUp &= req.isUp();
                judDown |= req.isUp();
            }
        }
        if (judUp && reqQueue.notHaveDirReq(floor, false) && !dirUp) {
            dirUp = true;
        } else if (!judDown && reqQueue.notHaveDirReq(floor, true) && dirUp) {
            dirUp = false;
        }
    }

    private void passengerOut() {
        if (isEmpty()) {
            return;
        }
        for (MyRequest req : passengers.get(floor - 1)) {
            SafeOutput.println("OUT-" + req.getId() + '-' + tower + '-' + floor + '-' + id);
        }
        size -= passengers.get(floor - 1).size();
        passengers.get(floor - 1).clear();
    }

    private void exchangePassenger() {
        if (needExchange()) {
            SafeOutput.println("OPEN-" + tower + '-' + floor + '-' + id);
            passengerOut();
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passengerIn();
            SafeOutput.println("CLOSE-" + tower + '-' + floor + '-' + id);
        }
    }

    private boolean needExchange() {
        if (!buffer.isEmpty()) {
            return true;
        }
        return !passengers.get(floor - 1).isEmpty();
    }

    public boolean isEmpty() {
        return size == 0;
    }

}
