import java.util.ArrayList;
import java.util.PriorityQueue;

public class WaitQueueVer {
    private ArrayList<PriorityQueue<User>> upWaiters;
    private ArrayList<PriorityQueue<User>> downWaiters;
    private boolean inputOver;
    private int wait;

    public WaitQueueVer() {
        upWaiters = new ArrayList<PriorityQueue<User>>();
        downWaiters = new ArrayList<PriorityQueue<User>>();
        for (int i = 0; i < 10; i++) {
            upWaiters.add(new PriorityQueue<User>());
            downWaiters.add(new PriorityQueue<User>());
        } inputOver = false;
        wait = 0;
    }

    public synchronized boolean isAllOver() { return inputOver && wait == 0; }

    public synchronized void trap() {
        try { wait(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public synchronized boolean noWaiterCur() {
        return wait == 0 && !inputOver;
    }

    public synchronized void setInputOver(boolean inputOver) {
        this.inputOver = inputOver;
        notifyAll();
    }

    public synchronized void addWaiter(User waiter) {
        wait++;
        int from = waiter.getFromFloor();
        int to = waiter.getTmpFloor();
        if (from < to) {
            upWaiters.get(from).add(waiter);
        } else { downWaiters.get(from).add(waiter); }
        notifyAll();
    }

    public synchronized int getUpWaiterNum(int floor) {
        return upWaiters.get(floor).size();
    }

    public synchronized int getDownWaiterNum(int floor) {
        return downWaiters.get(floor).size();
    }

    public synchronized int getInUp(int limit, int load, int floor,
                                    ArrayList<ArrayList<User>> passengers, int eleId) {
        int retLoad = load;
        User in;
        while (retLoad < limit && upWaiters.get(floor).size() != 0) {
            in = upWaiters.get(floor).remove();
            passengers.get(in.getTmpFloor()).add(in);
            Output.out("IN-" + in.getId() + "-" + (char)(in.getFromZuo() + 'A') +
                    "-" + (in.getFromFloor() + 1) + "-" + eleId);
            retLoad++;
            wait--;
        } return retLoad;
    }

    public synchronized int getInDown(int limit, int load, int floor,
                                      ArrayList<ArrayList<User>> passengers, int eleId) {
        int retLoad = load;
        User in;
        while (retLoad < limit && downWaiters.get(floor).size() != 0) {
            in = downWaiters.get(floor).remove();
            passengers.get(in.getTmpFloor()).add(in);
            Output.out("IN-" + in.getId() + "-" + (char)(in.getFromZuo() + 'A') +
                    "-" + (in.getFromFloor() + 1) + "-" + eleId);
            retLoad++;
            wait--;
        } return retLoad;
    }

    public synchronized int getUpSum(int floor) {
        int ret = 0;
        for (int i = floor + 1; i < 10; i++) {
            ret += upWaiters.get(i).size();
            ret += downWaiters.get(i).size();
        } return ret;
    }

    public synchronized int getDownSum(int floor) {
        int ret = 0;
        for (int i = floor - 1; i >= 0; i--) {
            ret += upWaiters.get(i).size();
            ret += downWaiters.get(i).size();
        } return ret;
    }

    public synchronized boolean hasWaiterUp(int floor) {
        for (int i = floor + 1; i < 10; i++) {
            if (upWaiters.get(i).size() != 0) { return true; }
            if (downWaiters.get(i).size() != 0) { return true; }
        } return false;
    }

    public synchronized boolean hasWaiterDown(int floor) {
        for (int i = floor - 1; i >= 0; i--) {
            if (upWaiters.get(i).size() != 0) { return true; }
            if (downWaiters.get(i).size() != 0) { return true; }
        } return false;
    }
}
