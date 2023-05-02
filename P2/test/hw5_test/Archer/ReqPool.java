import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class ReqPool {
    //0号存总信息
    private final ArrayList<HashSet<PersonRequest>> up;
    private final ArrayList<HashSet<PersonRequest>> down;
    private boolean end;

    public ReqPool() {
        up = new ArrayList<>();
        down = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            HashSet<PersonRequest> curUp = new HashSet<>();
            HashSet<PersonRequest> curDown = new HashSet<>();
            up.add(curUp);
            down.add(curDown);
        }
        this.end = false;
    }

    public synchronized boolean isEnd() { return end; }

    public synchronized void stop() {
        this.end = true;
        notifyAll();
    }

    public synchronized boolean isEmpty() { return up.get(0).isEmpty() && down.get(0).isEmpty(); }

    public synchronized int size() { return up.get(0).size() + down.get(0).size(); }

    public synchronized void add(PersonRequest p) {
        if (p.getFromFloor() < p.getToFloor()) {
            up.get(p.getFromFloor()).add(p);
            up.get(0).add(p);
        }
        else {
            down.get(p.getFromFloor()).add(p);
            down.get(0).add(p);
        }
        notifyAll();
    }

    public synchronized void remove(PersonRequest p) {
        if (p.getFromFloor() < p.getToFloor()) {
            up.get(p.getFromFloor()).remove(p);
            up.get(0).remove(p);
        }
        else {
            down.get(p.getFromFloor()).remove(p);
            down.get(0).remove(p);
        }
    }

    public synchronized HashSet<PersonRequest> getUp(int f) { return up.get(f); }

    public synchronized HashSet<PersonRequest> getDown(int f) { return down.get(f); }

    public synchronized int fwdCnt(boolean dir, int f) {
        int ret = 0;
        if (dir) {
            for (int i = f + 1; i <= 10; i++) {
                ret += up.get(i).size() + down.get(i).size();
            }
        }
        else {
            for (int i = f - 1; i >= 1; i--) {
                ret += up.get(i).size() + down.get(i).size();
            }
        }
        return ret;
    }
}
