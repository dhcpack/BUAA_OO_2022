
import java.util.ArrayList;

public class WaitBufferPor { //纵向列表 10*n
    private ArrayList<ArrayList<PersonRequest2>> waitEntry;//只对该电梯可见的一个楼座的等待列表
    private Scheduler sc;
    private int isend;

    public synchronized int getIsend() {
        return isend;
    }

    public void setSc(Scheduler sc) {
        this.sc = sc;
    }

    //一个二维数组 11*n，只用后10个
    public WaitBufferPor() {
        waitEntry = new ArrayList<>();
        isend = Const.PRONOTEND;
        for (int i = 0; i <= Const.MAXNUM_BUILDING; i++) { //创11个数组，不用第一个，方便直接从1开始
            ArrayList<PersonRequest2> newp = new ArrayList<>();
            waitEntry.add(newp);
        }
    }

    public synchronized void put(PersonRequest2 request) {
        //System.out.println("addrequest");
        waitEntry.get(request.getFromFloor()).add(request);
        //System.out.println("notify");
        notifyAll();//awake
    }

    public synchronized void putend() {
        isend = Const.PROEND;
        notifyAll();
    }

    public synchronized void check() {
        //        System.out.println(isend);
        while (blockisempty() && isend != Const.PROEND) { // 空表且程序没结束，电梯等待唤醒
            try {
                //System.out.println("startcheck");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("endcheck");
        //不空的话可以结束REST
    }

    public synchronized int getdir(PersonRequest2 request) {
        if (request.getToFloor() > request.getFromFloor()) {
            return Const.UP;
        } else {
            return Const.DOWN;
        }
    }

    public synchronized void getpassenger(Elevatorpor e) { //look考虑方向
        int curfloor = e.getEleStatus().getCurrentfloor();
        if (floorisempty(curfloor)) {
            return;
        }
        if (!e.elefull() && !this.floorisempty(curfloor)) {
            if (e.eleempty()) {
                redirifempty(e);
            }
            ArrayList<PersonRequest2> plist = waitEntry.get(curfloor);
            for (int i = 0; i < plist.size(); i++) {
                PersonRequest2 request = plist.get(i);
                if (!e.elefull() && getdir(request) == e.getEleStatus().getDirect()) { //同方向进入
                    e.getEleStatus().getPassenger().add(request);
                    OutputThread.println("IN-" + request.getPersonId()
                            + "-" + e.getCurblock()
                            + "-" + e.getEleStatus().getCurrentfloor() + "-" + e.getEleid());
                    plist.remove(request);
                    i--;
                }
                if (e.elefull()) {
                    break;
                }
            }
        }
    }

    public synchronized void redirifempty(Elevatorpor e) { //如果此时电梯是空的话需要重新定一下方向
        int curfloor = e.getEleStatus().getCurrentfloor();
        ArrayList<PersonRequest2> plist = waitEntry.get(curfloor);
        int upnum = 0;
        int downnum = 0;
        for (PersonRequest2 request : plist) {
            if (getdir(request) == Const.UP) {
                upnum++;
            } else {
                downnum++;
            }
        }
        if (upnum >= downnum) { //谁多方向就定谁
            e.getEleStatus().setDirect(Const.UP);
        } else {
            e.getEleStatus().setDirect(Const.DOWN);
        }
    }

    public synchronized int getblocksize() {
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += getfloorsize(i);
        }
        return sum;
    }

    public synchronized int getfloorsize(int i) {
        return waitEntry.get(i).size();
    }

    public synchronized boolean blockisempty() {
        return getblocksize() == 0;
    }

    public synchronized boolean floorisempty(int i) {
        return getfloorsize(i) == 0;
    }

    public synchronized boolean sbneedin(Elevatorpor e) {
        int curfloor = e.getEleStatus().getCurrentfloor();
        ArrayList<PersonRequest2> plist = waitEntry.get(curfloor);
        if (floorisempty(curfloor)) {
            return false;
        }
        if (e.eleempty()) { //电梯是空的谁都能上
            return true;
        }
        for (PersonRequest2 request : plist) {
            if (getdir(request) == e.getEleStatus().getDirect()) { //同方向有人需要进
                return true;
            }
        }
        return false;
    }

    public synchronized boolean dirHasRQ(int direct, int floor) {
        boolean flag = false;
        if (direct == Const.UP) {
            if (floor == Const.MAXNUM_BUILDING) {
                return false;//do not has RQ
            }
            for (int i = floor + 1; i <= Const.MAXNUM_BUILDING; i++) {
                if (!floorisempty(i)) {
                    flag = true;
                    break;
                }
            }
        } else {
            if (floor == Const.MINFLOORS) {
                return false;
            }
            for (int i = floor - 1; i >= Const.MINFLOORS; i--) {
                if (!floorisempty(i)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
