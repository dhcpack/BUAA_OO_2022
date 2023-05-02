
import java.util.ArrayList;

public class Elevatorcro extends Thread { //横向
    private long movetime = 600;//ms
    private long opentime = 200;//ms
    private long closetime = 200;//ms
    private int max = 8;
    private int eleid;
    private int curfloor;
    private StrategyCro strategy;//strategy
    private WaitBufferCro waitlist;//waitlist
    private Scheduler scheduler;
    private EleStatus eleStatus;
    private ArrayList<Integer> canarrive;
    private WholeBuffer whole;

    public int getCurfloor() {
        return curfloor;
    }

    public int getEleid() {
        return eleid;
    }

    public ArrayList<Integer> getCanarrive() {
        return canarrive;
    }

    public Elevatorcro(WaitBufferCro wait, Scheduler sc, int floor, int id, WholeBuffer wo) {
        waitlist = wait;
        scheduler = sc;
        eleStatus = new EleStatus();
        curfloor = floor;
        strategy = new StrategyCro();
        eleid = id;
        canarrive = new ArrayList<>();
        for (int i = 0; i <= Const.CANMAX; i++) { //造6个，只用1开始，便于和block编号对应
            canarrive.add(1);
        }
        whole = wo;
        waitlist.addcan(canarrive);
        waitlist.setSc(scheduler);
    }

    public Elevatorcro(WaitBufferCro wait, Scheduler sc, int floor, int id, double mvtime,
                       int cap, ArrayList<Integer> can, WholeBuffer wo) {
        waitlist = wait;
        scheduler = sc;
        eleStatus = new EleStatus();
        curfloor = floor;
        strategy = new StrategyCro();
        eleid = id;
        movetime = (long) (mvtime * 1000);
        max = cap;
        canarrive = can;
        whole = wo;
        waitlist.addcan(canarrive);
        waitlist.setSc(scheduler);
    }

    @Override
    public void run() {
        while (true) {
            switch (eleStatus.getStatus()) {
                case Const.REST:
                    //System.out.println("REST");
                    waitlist.check(canarrive);
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
            char blo = (char) (eleStatus.getCurrentblock() - 1 + 'A');
            OutputThread.println("OPEN-" + blo + "-"
                    + curfloor + "-" + eleid);
            passengerout();//first out next in
            passengerin();
            sleep(opentime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public EleStatus getEleStatus() {
        return eleStatus;
    }

    public void close() {
        try {
            sleep(closetime);
            passengerin();
            char blo = (char) (eleStatus.getCurrentblock() - 1 + 'A');
            OutputThread.println("CLOSE-" +
                    blo + "-" + curfloor + "-" + eleid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void move() {
        try {
            strategy.look(this, waitlist);//change floor and dir
            sleep(movetime);
            eleStatus.setCurrentblock(eleStatus.getNextBlock());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void arrive() {
        char blo = (char) (eleStatus.getCurrentblock() - 1 + 'A');
        OutputThread.println("ARRIVE" + "-" + blo
                + "-" + curfloor + "-" + eleid);
    }

    public void passengerin() {
        waitlist.getpassenger(this);
    }

    public void passengerout() {
        ArrayList<PersonRequest2> plist = eleStatus.getPassenger();
        char blo = (char) (eleStatus.getCurrentblock() - 1 + 'A');
        int toblock;
        for (int i = 0; i < plist.size(); i++) {
            PersonRequest2 p = plist.get(i);
            toblock = p.getToBuilding() - 'A' + 1;
            if (toblock == eleStatus.getCurrentblock()) {
                //System.out.println(curfloor + " "+eleStatus.getCurrentblock());
                //System.out.println(p.getToFloor()+" "+p.getToBuilding());
                //                if(p.isarrive(curfloor,eleStatus.getCurrentblock())) {
                //                    OutputThread.println("OUT-" + p.getPersonId() + "-" +
                //                            blo + "-" + curfloor
                //                            + "-" + eleid+"-end");
                //                } else {
                //                    OutputThread.println("OUT-" + p.getPersonId() + "-" +
                //                            blo + "-" + curfloor
                //                            + "-" + eleid);
                //                }
                OutputThread.println("OUT-" + p.getPersonId() + "-" +
                            blo + "-" + curfloor
                            + "-" + eleid);
                plist.remove(p);
                //if(!p.isarrive(curfloor,eleStatus.getCurrentblock())) {
                p.setFromFloor(curfloor);
                char curblo;
                curblo = (char) (eleStatus.getCurrentblock() - 1 + 'A');
                p.setFromBuilding(curblo);
                reput(p); //重新放回等待转乘
                //}
                i--;
            }
        }
    }

    public void reput(PersonRequest2 p) {
        p.outcro();
        //put back
        whole.put(p);
    }

    public boolean eleempty() {
        return this.eleStatus.getPassenger().size() == 0;
    }

    public boolean elefull() {
        return this.eleStatus.getPassenger().size() == max;
    }

    public boolean sbneedout(int block) {
        int toblock;
        for (PersonRequest2 request : eleStatus.getPassenger()) {
            toblock = request.getToBuilding() - 'A' + 1;
            if (toblock == block) {
                return true;
            }
        }
        return false;
    }
}
