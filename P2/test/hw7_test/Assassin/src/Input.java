import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;

public class Input extends Thread {
    private Scheduler scheduler;
    private WaitBufferPor wlista;
    private WaitBufferPor wlistb;
    private WaitBufferPor wlistc;
    private WaitBufferPor wlistd;
    private WaitBufferPor wliste;
    private WaitBufferCro wlist1;
    private WaitBufferCro wlist2;
    private WaitBufferCro wlist3;
    private WaitBufferCro wlist4;
    private WaitBufferCro wlist5;
    private WaitBufferCro wlist6;
    private WaitBufferCro wlist7;
    private WaitBufferCro wlist8;
    private WaitBufferCro wlist9;
    private WaitBufferCro wlist10;
    private WholeBuffer whole;

    public Input(Scheduler s, WholeBuffer wholeBuffer, WaitBufferPor wa,
                 WaitBufferPor wb, WaitBufferPor wc, WaitBufferPor wd,
                 WaitBufferPor we, WaitBufferCro w1, WaitBufferCro w2, WaitBufferCro w3,
                 WaitBufferCro w4, WaitBufferCro w5, WaitBufferCro w6, WaitBufferCro w7,
                 WaitBufferCro w8, WaitBufferCro w9, WaitBufferCro w10) {
        scheduler = s;
        whole = wholeBuffer;
        wlista = wa;
        wlistb = wb;
        wlistc = wc;
        wlistd = wd;
        wliste = we;
        wlist1 = w1;
        wlist2 = w2;
        wlist3 = w3;
        wlist4 = w4;
        wlist5 = w5;
        wlist6 = w6;
        wlist7 = w7;
        wlist8 = w8;
        wlist9 = w9;
        wlist10 = w10;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                whole.setReadend(Const.PROEND);//end
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest r = (PersonRequest) request;
                    PersonRequest2 p = new PersonRequest2(r);
                    whole.put(p); //放入总区
                } else if (request instanceof ElevatorRequest) {
                    //add ele ------------------------
                    ElevatorRequest er = (ElevatorRequest) request;
                    if (er.getType().equals("floor")) { //楼层
                        addCor(er);
                    } else { //楼座
                        addPor(er);
                    }
                }

            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPor(ElevatorRequest r) {
        Elevatorpor newele;
        int cap = r.getCapacity();
        int id = r.getElevatorId();
        double speed = r.getSpeed();
        switch (r.getBuilding()) {
            case 'A':
                newele = new Elevatorpor(wlista, scheduler, 'A', id, speed, cap, whole);
                newele.start();
                break;
            case 'B':
                newele = new Elevatorpor(wlistb, scheduler, 'B', id, speed, cap, whole);
                newele.start();
                break;
            case 'C':
                newele = new Elevatorpor(wlistc, scheduler, 'C', id, speed, cap, whole);
                newele.start();
                break;
            case 'D':
                newele = new Elevatorpor(wlistd, scheduler, 'D', id, speed, cap, whole);
                newele.start();
                break;
            case 'E':
                newele = new Elevatorpor(wliste, scheduler, 'E', id, speed, cap, whole);
                newele.start();
                break;
            default:
                break;
        }
    }

    public void addCor(ElevatorRequest r) {
        Elevatorcro newele;
        int cap = r.getCapacity();
        int id = r.getElevatorId();
        double speed = r.getSpeed();
        ArrayList<Integer> can;
        can = Const.getcanArrive(r.getSwitchInfo());
        switch (r.getFloor()) {
            case 1:
                newele = new Elevatorcro(wlist1, scheduler, 1, id, speed, cap, can, whole);
                newele.start();
                break;
            case 2:
                newele = new Elevatorcro(wlist2, scheduler, 2, id, speed, cap, can, whole);
                newele.start();
                break;
            case 3:
                newele = new Elevatorcro(wlist3, scheduler, 3, id, speed, cap, can, whole);
                newele.start();
                break;
            case 4:
                newele = new Elevatorcro(wlist4, scheduler, 4, id, speed, cap, can, whole);
                newele.start();
                break;
            case 5:
                newele = new Elevatorcro(wlist5, scheduler, 5, id, speed, cap, can, whole);
                newele.start();
                break;
            case 6:
                newele = new Elevatorcro(wlist6, scheduler, 6, id, speed, cap, can, whole);
                newele.start();
                break;
            case 7:
                newele = new Elevatorcro(wlist7, scheduler, 7, id, speed, cap, can, whole);
                newele.start();
                break;
            case 8:
                newele = new Elevatorcro(wlist8, scheduler, 8, id, speed, cap, can, whole);
                newele.start();
                break;
            case 9:
                newele = new Elevatorcro(wlist9, scheduler, 9, id, speed, cap, can, whole);
                newele.start();
                break;
            case 10:
                newele = new Elevatorcro(wlist10, scheduler, 10, id, speed, cap, can, whole);
                newele.start();
                break;
            default:
                break;
        }
    }
}
