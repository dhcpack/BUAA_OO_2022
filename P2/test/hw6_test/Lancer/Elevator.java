import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread implements FromAndTo {
    private final ArrayList<Integer> reachAble;
    private final ArrayList<Integer> stoppable;
    private final int num;
    private final int capacity;
    private final char building;
    private final int floor;
    private final int movePerFloorTime;
    private final int openTime;
    private final int closeTime;

    public Elevator(ArrayList<Integer> reachAble, ArrayList<Integer> stoppable,
                    int id, int capacity, char building, int floor,
                    int movePerFloorTime, int openTime, int closeTime, int beginFloor) {
        this.reachAble = reachAble;
        this.stoppable = stoppable;
        this.num = id;
        this.capacity = capacity;
        this.building = building;
        this.floor = floor;
        this.movePerFloorTime = movePerFloorTime;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.current = beginFloor;
    }

    private int passengerCnt = 0;
    private int current;
    private int direction = 0;
    private PersonRequest mainRequest = null;

    private final Queue queue = new Queue();

    public Queue getQueue() {
        return queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (!queue.isEmpty()) {
                    if (mainRequest == null) {
                        mainRequest = queue.getMainRequest(current);
                        updateDirection();
                    }
                    try {
                        open();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!queue.isEmpty()) {
                        try {
                            go();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                while (queue.isEmpty()) {
                    if (queue.isEnd()) {
                        //下线
                        return;
                    } else {
                        queue.notifyAll();
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                queue.notifyAll();
            }
        }
    }

    public void go() throws InterruptedException {
        int next = getNext();
        if (direction != 0) {
            if (reachAble.contains(next)) {
                current = next;

                queue.notifyAll();
                queue.setSleep(true);
                queue.wait(movePerFloorTime);
                queue.setSleep(false);

                arrivePrint();
            }
        }

        updateDirection();
    }

    public int getNext() {
        return current + direction;
    }

    public void open() throws InterruptedException {
        ArrayList<PersonRequest> in = queue.inRequest(current);
        ArrayList<PersonRequest> out = queue.outRequest(current);
        if (((!in.isEmpty()) || (!out.isEmpty())) && (stoppable.contains(current))) {
            //可以开门
            openPrint();
            queue.notifyAll();
            queue.setSleep(true);
            queue.wait(openTime + closeTime);
            queue.setSleep(false);
            //上下人
            in = queue.inRequest(current);
            out = queue.outRequest(current);
            boolean flag = false;
            for (PersonRequest p : out) {
                queue.removeRunning(p);
                passengerCnt -= 1;
                outPrint(p.getPersonId());
                if (p == mainRequest) {
                    flag = true;
                }
            }
            if (flag) {
                mainRequest = queue.getMainRequest(current);
                updateDirection();
                flag = false;
            }
            for (PersonRequest p : in) {
                if ((passengerCnt < capacity) &&
                        ((getTo(p) - getFrom(p) > 0) == (direction > 0))) {
                    queue.addRunning(p);
                    queue.removeWait(p);
                    passengerCnt += 1;
                    inPrint(p.getPersonId());
                }
                if (p == mainRequest) {
                    flag = true;
                }
            }
            if (flag) {
                mainRequest = queue.getMainRequest(current);
                updateDirection();
            }
            closePrint();
        }
    }

    public void print(String output) {
        synchronized (Print.getInstance()) {
            Print.getInstance().print(output);
        }
    }

    public void updateDirection() {
        /*if(queue.isEmpty()){
            direction=0;
        }else if(currentFloor==1&&direction==-1){
            direction=1;
        }else if(currentFloor==10&&direction==1) {
            direction = -1;
        }*/

        if (mainRequest == null) {
            direction = 0;
        } else if (queue.isRunning(mainRequest)) {
            direction = (getTo(mainRequest) - getFrom(mainRequest)) > 0 ? 1 : -1;
        } else {
            if (getFrom(mainRequest) == current) {
                direction = (getTo(mainRequest) - getFrom(mainRequest)) > 0 ? 1 : -1;
            } else {
                direction = (getFrom(mainRequest) - current) > 0 ? 1 : -1;
            }
        }
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

    public ArrayList<Integer> getReachAble() {
        return reachAble;
    }

    public ArrayList<Integer> getStoppable() {
        return stoppable;
    }

    public int getNUm() {
        return num;
    }

    public int getCapacity() {
        return capacity;
    }

    public char getBuilding() {
        return building;
    }

    public int getMovePerFloorTime() {
        return movePerFloorTime;
    }

    public int getOpenTime() {
        return openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public int getFloor() {
        return floor;
    }

    public int getPassengerCnt() {
        return passengerCnt;
    }

    public int getCurrent() {
        return current;
    }

    public int getDirection() {
        return direction;
    }

    public PersonRequest getMainRequest() {
        return mainRequest;
    }

    public void arrivePrint() {
        print("ARRIVE" + '-' + building + "-" + current + "-" + num);
    }

    public void openPrint() {
        print("OPEN" + '-' + building + "-" + current + "-" + num);
    }

    public void closePrint() {
        print("CLOSE" + '-' + building + "-" + current + "-" + num);
    }

    public void inPrint(int passengerId) {
        print("IN" + '-' + passengerId + "-" + building + "-" + current + "-" + num);
    }

    public void outPrint(int passengerId) {
        print("OUT" + '-' + passengerId + "-" + building + "-" + current + "-" + num);
    }
}
