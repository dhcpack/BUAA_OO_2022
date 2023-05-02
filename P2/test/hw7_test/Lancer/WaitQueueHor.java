import java.util.ArrayList;

public class WaitQueueHor {
    private ArrayList<ArrayList<User>> rightWaiters;
    private ArrayList<ArrayList<User>> leftWaiters;
    private boolean inputOver;
    private int wait;

    public WaitQueueHor() {
        rightWaiters = new ArrayList<ArrayList<User>>();
        leftWaiters = new ArrayList<ArrayList<User>>();
        for (int i = 0; i < 5; i++) {
            rightWaiters.add(new ArrayList<>());
            leftWaiters.add(new ArrayList<>());
        } inputOver = false;
        wait = 0;
    }

    public synchronized boolean isAllOver(int sw) {
        return inputOver && getWait(sw) == 0;
    }

    public synchronized void trap() {
        try { wait(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public synchronized boolean noWaiterCur(int sw) {
        return getWait(sw) == 0 && !inputOver;
    }

    public synchronized void setInputOver(boolean inputOver) {
        this.inputOver = inputOver;
        notifyAll();
    }

    public synchronized void addWaiter(User waiter) {
        wait++;
        int from = waiter.getFromZuo();
        int to = waiter.getTmpZuo();
        int i = 0;
        ArrayList<User> waiters = null;
        if ((from + 1) % 5 == to || (from + 2) % 5 == to) {
            waiters = rightWaiters.get(from);
        } else { waiters = leftWaiters.get(from); }
        for (i = 0; i < waiters.size(); i++) {
            if (waiter.getDist() < waiters.get(i).getDist()) { break; }
        } waiters.add(i, waiter);
        notifyAll();
    }

    public synchronized int getValid(ArrayList<User> waiters, int from, int sw) {
        if ((sw & (1 << from)) == 0) { return 0; }
        int ret = 0;
        for (int i = 0; i < waiters.size(); i++) {
            User user = waiters.get(i);
            if ((sw & (1 << user.getTmpZuo())) != 0) { ret++; }
        } return ret;
    }

    public synchronized int getWait(int sw) {
        int cnt = 0;
        for (int i = 0; i < leftWaiters.size(); i++) {
            cnt += getValid(leftWaiters.get(i), i, sw);
        }
        for (int i = 0; i < rightWaiters.size(); i++) {
            cnt += getValid(rightWaiters.get(i), i, sw);
        } return cnt;
    }

    public synchronized int getRightWaiterNum(int building, int sw) {
        return getValid(rightWaiters.get(building), building, sw);
    }

    public synchronized int getLeftWaiterNum(int building, int sw) {
        return getValid(leftWaiters.get(building), building, sw);
    }

    public synchronized int getInRight(int limit, int load, int building,
                                    ArrayList<ArrayList<User>> passengers, int eleId, int sw) {
        if ((sw & (1 << building)) == 0) { return load; }
        int retLoad = load;
        User in;
        ArrayList<User> waiters = rightWaiters.get(building);
        for (int i = 0; i < waiters.size() && retLoad < limit; i++) {
            in = waiters.get(i);
            if ((sw & (1 << in.getTmpZuo())) == 0) { continue; }
            waiters.remove(i);
            passengers.get(in.getTmpZuo()).add(in);
            Output.out("IN-" + in.getId() + "-" + (char)(in.getFromZuo() + 'A') +
                    "-" + (in.getFromFloor() + 1) + "-" + eleId);
            retLoad++;
            wait--;
        } return retLoad;
    }

    public synchronized int getInLeft(int limit, int load, int building,
                                      ArrayList<ArrayList<User>> passengers, int eleId, int sw) {
        if ((sw & (1 << building)) == 0) { return load; }
        int retLoad = load;
        User in;
        ArrayList<User> waiters = leftWaiters.get(building);
        for (int i = 0; i < waiters.size() && retLoad < limit; i++) {
            in = waiters.get(i);
            if ((sw & (1 << in.getTmpZuo())) == 0) { continue; }
            waiters.remove(i);
            passengers.get(in.getTmpZuo()).add(in);
            Output.out("IN-" + in.getId() + "-" + (char)(in.getFromZuo() + 'A') +
                    "-" + (in.getFromFloor() + 1) + "-" + eleId);
            retLoad++;
            wait--;
        } return retLoad;
    }

    public synchronized int getRightSum(int building, int sw) {
        int ret = 0;
        ret += getValid(rightWaiters.get((building + 1) % 5), (building + 1) % 5, sw);
        ret += getValid(leftWaiters.get((building + 1) % 5), (building + 1) % 5, sw);
        ret += getValid(rightWaiters.get((building + 2) % 5), (building + 2) % 5, sw);
        ret += getValid(leftWaiters.get((building + 2) % 5), (building + 2) % 5, sw);
        return ret;
    }

    public synchronized int getLeftSum(int building, int sw) {
        int ret = 0;
        ret += getValid(rightWaiters.get((building + 4) % 5), (building + 4) % 5, sw);
        ret += getValid(leftWaiters.get((building + 4) % 5), (building + 4) % 5, sw);
        ret += getValid(rightWaiters.get((building + 3) % 5), (building + 3) % 5, sw);
        ret += getValid(leftWaiters.get((building + 3) % 5), (building + 3) % 5, sw);
        return ret;
    }

    public synchronized boolean hasRWaiterRight(int building, int sw) {
        if (getValid(rightWaiters.get((building + 1) % 5), (building + 1) % 5, sw) != 0) {
            return true;
        } if (getValid(rightWaiters.get((building + 2) % 5), (building + 2) % 5, sw) != 0) {
            return true;
        } return false;
    }

    public synchronized boolean hasLWaiterLeft(int building, int sw) {
        if (getValid(leftWaiters.get((building + 4) % 5), (building + 4) % 5, sw) != 0) {
            return true;
        } if (getValid(leftWaiters.get((building + 3) % 5), (building + 3) % 5, sw) != 0) {
            return true;
        } return false;
    }

    public synchronized int getNearestDir(int building, int sw) {
        if (getWait(sw) == 0 || getRightWaiterNum(building, sw) +
                getLeftWaiterNum(building, sw) != 0) { return 0; }
        int r;
        int l;
        for (r = 1; r <= 3; r++) {
            if (getRightWaiterNum((building + r) % 5, sw) != 0) { break; }
        } for (l = 1; l <= 3; l++) {
            if (getLeftWaiterNum((building - l + 5) % 5, sw) != 0) { break; }
        } if (r <= l) { return 1; }
        return 2;
    }
}
