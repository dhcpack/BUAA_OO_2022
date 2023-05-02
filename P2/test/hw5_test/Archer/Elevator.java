import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.HashSet;
import java.util.Iterator;

public class Elevator extends Thread {
    public static final int CAPACITY = 6;
    private final char block;
    private final int id;
    private final ReqPool pool;
    private final HashSet<PersonRequest> pax;
    private final Dispatcher dispatcher;
    private int cur;
    private Status status;
    private long startT;

    public Elevator(char block, ReqPool pool) {
        this.block = block;
        this.id = block - 'A' + 1;
        this.pool = pool;
        this.dispatcher = new Dispatcher(pool, this);
        this.cur = 1;
        this.status = Status.Halt;
        this.startT = 0;
        this.pax = new HashSet<>();
    }

    public int getCur() { return cur; }

    public Status getStatus() { return status; }

    public HashSet<PersonRequest> getPax() { return pax; }

    @Override
    public void run() {
        while (true) {
            if (pool.isEmpty() && pax.isEmpty() && pool.isEnd()) { break; }
            try {
                startT = System.currentTimeMillis();
                Status nextS = dispatcher.nextOp();
                switch (nextS) {
                    case Switch:
                        TimableOutput.println("OPEN-" + block + '-' + cur + '-' + id);
                        drop();
                        sleep(400);
                        pick();
                        TimableOutput.println("CLOSE-" + block + '-' + cur + '-' + id);
                        break;
                    case Up:
                    case Down:
                        boolean up = nextS == Status.Up;
                        boolean chosenGot = false;
                        if (pax.size() == CAPACITY) { sleep(400); }
                        else {
                            synchronized (pool) {
                                while (System.currentTimeMillis() - startT < 400) {
                                    pool.wait(400 - (System.currentTimeMillis() - startT));
                                    chosenGot = up ? pool.getUp(cur).size() > 0 :
                                            pool.getDown(cur).size() > 0;
                                    if (chosenGot) { break; }
                                }
                            }
                            if (chosenGot) { break; }
                        }
                        status = nextS;
                        cur = up ? cur + 1 : cur - 1;
                        TimableOutput.println("ARRIVE-" + block + '-' + cur + '-' + id);
                        break;
                    case Halt:
                        status = Status.Halt;
                        synchronized (pool) { pool.wait(); }
                        break;
                    default:
                        throw new Exception();
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        //System.out.println(block + " elev quit");
    }

    private void drop() {
        Iterator<PersonRequest> i = pax.iterator();
        while (i.hasNext()) {
            PersonRequest p = i.next();
            if (p.getToFloor() == cur) {
                TimableOutput.println(
                        "OUT-" + p.getPersonId() + '-' + block + '-' + cur + '-' + id);
                i.remove();
            }
        }
    }

    private void pick() {
        dispatcher.update();
        HashSet<PersonRequest> upReq = pool.getUp(cur);
        HashSet<PersonRequest> downReq = pool.getDown(cur);
        HashSet<PersonRequest> allReq = new HashSet<>(upReq);
        allReq.addAll(downReq);
        allReq.forEach(p -> {
            if (dispatcher.getChosen().contains(p)) {
                TimableOutput.println("IN-" + p.getPersonId() + '-' + block + '-' + cur + '-' + id);
                pax.add(p);
                status = p.getToFloor() > p.getFromFloor() ? Status.Up : Status.Down;
                pool.remove(p);
            }
        });
    }

    public enum Status { Switch, Up, Down, Halt }
}
