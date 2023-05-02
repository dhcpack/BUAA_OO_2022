import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Queue implements FromAndTo {
    private boolean end = false;
    private boolean sleep = false;
    private final ArrayList<PersonRequest> wait = new ArrayList<>();
    private final ArrayList<PersonRequest> running = new ArrayList<>();

    //如果锁住了Queue，那么此时对wait和running都不能进行操作，其实他们是安全的。。
    public synchronized boolean isEnd() {
        return end;
    }

    public synchronized void setEnd(boolean end) {
        this.end = end;
    }

    public synchronized boolean isSleep() {
        return sleep;
    }

    public synchronized void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public synchronized boolean isEmpty() {
        return wait.isEmpty() && running.isEmpty();
    }

    public synchronized void addWait(PersonRequest request) {
        wait.add(request);
        //System.out.println("add wait"+request);
    }

    public synchronized void addRunning(PersonRequest request) {
        running.add(request);
    }

    public synchronized void removeWait(PersonRequest request) {
        wait.remove(request);
    }

    public synchronized void removeRunning(PersonRequest request) {
        running.remove(request);
    }

    public synchronized ArrayList<PersonRequest> inRequest(int current) {
        //要上电梯的
        ArrayList<PersonRequest> in = new ArrayList<>();
        for (PersonRequest p : wait) {
            if (getFrom(p) == current) {
                in.add(p);
                //System.out.println(p);
            }
        }
        return in;
    }

    public synchronized int getFrom(PersonRequest p) {
        if (p.getFromFloor() != p.getToFloor()) {
            return p.getFromFloor();
        } else if (p.getFromBuilding() != p.getToBuilding()) {
            return p.getFromBuilding();
        } else {
            return -1;
        }
    }

    public synchronized int getTo(PersonRequest p) {
        if (p.getFromFloor() != p.getToFloor()) {
            return p.getToFloor();
        } else if (p.getFromBuilding() != p.getToBuilding()) {
            return p.getToBuilding();
        } else {
            return -1;
        }
    }

    //获取完一定要记得删掉
    public synchronized ArrayList<PersonRequest> outRequest(int curent) {
        ArrayList<PersonRequest> out = new ArrayList<>();
        for (PersonRequest p : running) {
            if (getTo(p) == curent) {
                out.add(p);
                //System.out.println(p);
            }
        }
        return out;
    }

    public synchronized PersonRequest getMainRequest(int floor) {
        PersonRequest main = null;
        if (!running.isEmpty()) {
            //取最早到达的
            main = earliestArrive(floor);
        } else if (!wait.isEmpty()) {
            main = wait.get(0);//请求时间最早
        }
        //System.out.println("main: "+main);
        return main;
    }

    public synchronized PersonRequest earliestArrive(int floor) {
        PersonRequest earliest = running.get(0);
        int distance = Math.abs(earliest.getToFloor() - floor);
        for (int i = 1; i < running.size(); i++) {
            int tmp = Math.abs(running.get(i).getToFloor() - floor);
            if (tmp < distance) {
                earliest = running.get(i);
                distance = tmp;
            }
        }
        return earliest;
    }

    public synchronized boolean isWaiting(PersonRequest p) {
        return wait.contains(p);
    }

    public synchronized boolean isRunning(PersonRequest p) {
        return running.contains(p);
    }

}
