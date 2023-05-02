
import java.util.ArrayList;

public class Elevatorpor extends Thread { //纵向
    private long movetime = 600;//ms
    private long opentime = 200;//ms
    private long closetime = 200;//ms
    private int max = 8;
    private int eleid;
    private char curblock;
    private StrategyPor strategy;//strategy
    private WaitBufferPor waitlist;//waitlist
    private Scheduler scheduler;
    private EleStatus eleStatus;
    private WholeBuffer whole;

    public char getCurblock() {
        return curblock;
    }

    public int getEleid() {
        return eleid;
    }

    public Elevatorpor(WaitBufferPor wait, Scheduler sc, char block, int id, WholeBuffer wo) {
        waitlist = wait;
        scheduler = sc;
        whole = wo;
        eleStatus = new EleStatus();
        curblock = block;
        strategy = new StrategyPor();
        eleid = id;
        waitlist.setSc(scheduler);
    }

    public Elevatorpor(WaitBufferPor wait, Scheduler sc, char block, int id, double mvtime
            , int cap, WholeBuffer wo) {
        waitlist = wait;
        scheduler = sc;
        whole = wo;
        eleStatus = new EleStatus();
        curblock = block;
        strategy = new StrategyPor();
        eleid = id;
        movetime = (long) (mvtime * 1000);
        max = cap;
        waitlist.setSc(scheduler);
    }

    public boolean eleempty() {
        return this.eleStatus.getPassenger().size() == 0;
    }

    public boolean elefull() {
        return this.eleStatus.getPassenger().size() == max;
    }

    public boolean sbneedout(int floor) {
        for (PersonRequest2 request : eleStatus.getPassenger()) {
            if (request.getToFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public EleStatus getEleStatus() {
        return eleStatus;
    }

    @Override
    public void run() {
        while (true) {
            switch (eleStatus.getStatus()) {
                case Const.REST:
                    //System.out.println("REST");
                    waitlist.check();
                    strategy.control(this, waitlist);
                    break;
                case Const.OPEN:
                    //System.out.println("OPEN");
                    open();
                    strategy.control(this, waitlist);
                    break;
                case Const.CLOSE:
                    //System.out.println("CLOSE");
                    close();
                    strategy.control(this, waitlist);
                    break;
                case Const.MOVE:
                    //System.out.println("MOVE");
                    move();
                    strategy.control(this, waitlist);
                    break;
                case Const.ARRIVE:
                    //System.out.println("ARRIVE");
                    arrive();
                    strategy.control(this, waitlist);
                    break;
                case Const.END:
                    break;
                default:
                    break;
            }
            if (eleStatus.getStatus() == Const.END) {
                break;
            }
        }
    }

    public void open() {
        try {
            OutputThread.println("OPEN-" + curblock + "-"
                    + eleStatus.getCurrentfloor() + "-" + eleid);
            passengerout();//first out next in
            passengerin();
            sleep(opentime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            sleep(closetime);
            passengerin();
            OutputThread.println("CLOSE-" +
                    curblock + "-" + eleStatus.getCurrentfloor() + "-" + eleid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        try {
            strategy.look(this, waitlist);//change floor and dir
            //System.out.println(totalmovetime);
            sleep(movetime);
            eleStatus.setCurrentfloor(eleStatus.getNextfloor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void arrive() {
        OutputThread.println("ARRIVE" + "-" + curblock
                + "-" + eleStatus.getCurrentfloor() + "-" + eleid);
    }

    public void passengerin() {
        //get passenger from waitbuffer
        waitlist.getpassenger(this);
    }

    public void passengerout() {
        ArrayList<PersonRequest2> plist = eleStatus.getPassenger();
        for (int i = 0; i < plist.size(); i++) {
            PersonRequest2 p = plist.get(i);
            if (p.getToFloor() == eleStatus.getCurrentfloor()) {
                //                int curblockint = curblock-'A'+1;
                //                if(p.isarrive(eleStatus.getCurrentfloor(),curblockint)) {
                //                    OutputThread.println("OUT-" + p.getPersonId() + "-" +
                //                            curblock + "-" + eleStatus.getCurrentfloor()
                //                            + "-" + eleid+"-end");
                //                } else {
                //                    OutputThread.println("OUT-" + p.getPersonId() + "-" +
                //                            curblock + "-" + eleStatus.getCurrentfloor()
                //                            + "-" + eleid);
                //                }
                OutputThread.println("OUT-" + p.getPersonId() + "-" +
                            curblock + "-" + eleStatus.getCurrentfloor()
                            + "-" + eleid);
                plist.remove(p);
                //if(!p.isarrive(eleStatus.getCurrentfloor(),curblock)) {
                p.setFromFloor(eleStatus.getCurrentfloor());
                p.setFromBuilding(curblock);
                reput(p); //重新放回等待转乘
                //}
                i--;
            }
        }
    }

    public void reput(PersonRequest2 p) {
        p.outpor();
        //put back
        whole.put(p);
    }
}
