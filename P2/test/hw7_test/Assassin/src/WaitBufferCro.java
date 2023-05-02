
import java.util.ArrayList;

public class WaitBufferCro { //横向列表 5*n
    private ArrayList<ArrayList<PersonRequest2>> waitEntry;//只对该电梯可见的一个楼座的等待列表
    private Scheduler sc;
    private Cans cans;
    private int isend;

    public synchronized void addcan(ArrayList<Integer> can) {
        cans.addcan(can);
    }

    public Cans getCans() {
        return cans;
    }

    public void setSc(Scheduler sc) {
        this.sc = sc;
    }

    public WaitBufferCro() {
        waitEntry = new ArrayList<>();
        cans = new Cans();
        isend = Const.PRONOTEND;
        for (int i = 0; i <= Const.MAXNUM_FLOOR; i++) { //创6个数组，从1开始即可
            ArrayList<PersonRequest2> newp = new ArrayList<>();
            waitEntry.add(newp);
        }
    }

    public synchronized int getIsend() {
        return isend;
    }

    public synchronized void put(PersonRequest2 request) {
        int i;
        i = request.getFromBuilding() - 'A' + 1;
        waitEntry.get(i).add(request);
        notifyAll();//awake
    }

    public synchronized void putend() {
        isend = Const.PROEND;
        notifyAll();
    }

    public synchronized void check(ArrayList<Integer> can) {
        while (floorisempty(can) && isend != Const.PROEND) { // 空表且程序没结束，电梯等待唤醒
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

    public synchronized void getpassenger(Elevatorcro e) { //look考虑方向
        int curblock = e.getEleStatus().getCurrentblock();
        if (blockisempty(curblock, e.getCanarrive())) {
            return;
        }
        if (!e.elefull() && !this.blockisempty(curblock, e.getCanarrive())) {
            ArrayList<PersonRequest2> plist = waitEntry.get(curblock);
            int dir = Const.getdir(plist.get(0));
            if (e.eleempty()) {
                e.getEleStatus().setDirect(dir);
            }

            for (int i = 0; i < plist.size(); i++) {
                PersonRequest2 request = plist.get(i);
                int toblock;
                toblock = request.getToBuilding() - 'A' + 1;
                if (!e.elefull()
                        && e.getCanarrive().get(toblock) == 1) { //
                    e.getEleStatus().getPassenger().add(request);
                    char blo = (char) (e.getEleStatus().getCurrentblock() - 1 + 'A');
                    OutputThread.println("IN-" + request.getPersonId()
                            + "-" + blo
                            + "-" + e.getCurfloor() + "-" + e.getEleid());
                    plist.remove(request);
                    i--;
                }
                if (e.elefull()) {
                    break;
                }
            }
        }
    }

    //    public synchronized int getfloorsize() {
    //        int sum = 0;
    //        for (int i = 1; i <= Const.MAXNUM_FLOOR; i++) {
    //            sum += getblocksize(i);
    //        }
    //        return sum;
    //    }

    //block 1-5 A-B
    //    public synchronized int getblocksize(int i) {
    //        return waitEntry.get(i).size();
    //    }

    //    public synchronized boolean floorisempty() {
    //        return getfloorsize() == 0;
    //    }

    public synchronized boolean floorisempty(ArrayList<Integer> can) {
        for (int i = 1; i <= Const.CANMAX; i++) {
            if (can.get(i) == 1) {
                if (!blockisempty(i, can)) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized boolean blockisempty(int b, ArrayList<Integer> can) { //i 1-5
        ArrayList<PersonRequest2> blist = waitEntry.get(b);
        for (int i = 0; i < blist.size(); i++) {
            PersonRequest2 p = blist.get(i);
            int fromblock = p.getFromBuilding() - 'A' + 1;
            int toblock = p.getToBuilding() - 'A' + 1;
            if (can.get(fromblock) == 1 && can.get(toblock) == 1) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean sbneedin(Elevatorcro e) {
        int curblock = e.getEleStatus().getCurrentblock();
        if (e.getCanarrive().get(curblock) == 0) {
            return false;
        }
        if (blockisempty(curblock, e.getCanarrive())) {
            return false;
        } else {
            ArrayList<PersonRequest2> list = waitEntry.get(curblock);
            for (PersonRequest2 p : list) {
                int toblock;
                toblock = p.getToBuilding() - 'A' + 1;
                if (e.getCanarrive().get(toblock) == 1) { //can get
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized int findmindir(int curblock, ArrayList<Integer> can) {
        if (curblock == 1) {
            if (!blockisempty(4, can) || !blockisempty(5, can)) {
                return Const.DOWN;
            } else {
                return Const.UP;
            }
        } else if (curblock == 2) {
            if (!blockisempty(1, can) || !blockisempty(5, can)) {
                return Const.DOWN;
            } else {
                return Const.UP;
            }
        } else if (curblock == 3) {
            if (!blockisempty(1, can) || !blockisempty(2, can)) {
                return Const.DOWN;
            } else {
                return Const.UP;
            }
        } else if (curblock == 4) {
            if (!blockisempty(2, can) || !blockisempty(3, can)) {
                return Const.DOWN;
            } else {
                return Const.UP;
            }
        } else if (curblock == 5) {
            if (!blockisempty(3, can) || !blockisempty(4, can)) {
                return Const.DOWN;
            } else {
                return Const.UP;
            }
        }
        return Const.UP;
    }
}
