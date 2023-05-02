import java.util.ArrayList;

public class FloorReqQueue {
    private static final int MAX_TOWER = 5;
    private final ArrayList<ArrayList<MyRequest>> reqs;
    private boolean end;
    private final int floor;
    private int size;

    public FloorReqQueue(int floor) {
        reqs = new ArrayList<>(MAX_TOWER + 1);
        for (int i = 0; i < MAX_TOWER; i++) {
            reqs.add(new ArrayList<>());
        }
        this.floor = floor;
        size = 0;
        end = false;
    }

    public synchronized void putReq(MyRequest req) {
        reqs.get(req.getFromTower()).add(req);
        size++;
        notifyAll();
    }

    public synchronized MyRequest takeReq(int tower) {
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
        return reqs.get(tower).remove(0);
    }

    public synchronized ArrayList<MyRequest> getReqs(int tower, int elevSize) {
        ArrayList<MyRequest> temp = new ArrayList<>();
        int count = elevSize;
        if (!haveReq(tower)) {
            return temp;
        }
        while (haveReq(tower) && count < 6) {
            temp.add(takeReq(tower));
            count++;
        }
        return temp;
    }

    public synchronized void end() {
        end = true;
        notifyAll();
    }

    public synchronized boolean haveReq(int tower) {
        return !reqs.get(tower).isEmpty();
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "<FloorQueue in floor: " + floor + ">";
    }
}