import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private boolean debug = false;
    private char building;
    private int id;
    private int openTime;
    private int closeTime;
    private int moveTime;
    private BuildingWaitQueue buildingWaitQueue;
    private int size;
    private boolean isOpen = false;
    private int nowFloor = 1;
    private int dir = 0;//0无方向，1向上，-1向下。
    private final ArrayList<Passenger> inElevatorPassenger = new ArrayList<>();

    public Elevator(char building, int id, int openTime,
                    int closeTime, int moveTime, BuildingWaitQueue buildingWaitQueue, int size) {
        this.building = building;
        this.id = id;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.moveTime = moveTime;
        this.buildingWaitQueue = buildingWaitQueue;
        this.size = size;
    }

    public void run() {
        while (true) {
            if (inElevatorPassenger.size() == 0 && buildingWaitQueue.needEnd()) {
                if (debug) {
                    System.out.println("elevator " + id + " end");
                }
                break;
            }
            if (inElevatorPassenger.size() == 0 && buildingWaitQueue.needWait()) {
                dir = 0;
            }
            if (!(inElevatorPassenger.size() == 0 && buildingWaitQueue.needEnd())) {
                look();
            }
        }
    }

    public void look() {
        try {
            if (inElevatorPassenger.size() == 0 && dir == 0) {
                int tarFloor = buildingWaitQueue.getNearFloor(nowFloor);
                if (tarFloor < nowFloor) {
                    move(-1);
                } else if (tarFloor > nowFloor) {
                    move(1);
                } else {
                    open();
                    close();
                }
            } else {
                move(dir);
                if ((size - inElevatorPassenger.size()) > 0
                        && buildingWaitQueue.hasPassenger(dir, nowFloor)
                        || hasArrivedPassenger()) {
                    open();
                    close();
                }
                if (inElevatorPassenger.size() == 0 &&
                        buildingWaitQueue.needChangeDir(nowFloor, dir)) {
                    dir = -dir;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move(int dir) throws InterruptedException {
        nowFloor += dir;
        sleep(moveTime);
        TimableOutput.println(String.format("ARRIVE-%c-%d-%d", building, nowFloor, id));
    }

    public void open() throws InterruptedException {
        TimableOutput.println(String.format("OPEN-%c-%d-%d", building, nowFloor, id));
        getOff();
        sleep(openTime);
    }

    public void getOff() {
        Iterator it = inElevatorPassenger.iterator();
        while (it.hasNext()) {
            Passenger passenger = (Passenger) it.next();
            if (passenger.getToFloor() == nowFloor && passenger.getToBuilding() == building) {
                TimableOutput.println(String.format("OUT-%d-%c-%d-%d",
                        passenger.getId(), building, nowFloor, id));
                it.remove();
            }
        }
    }

    public void getIn() {
        if (size - inElevatorPassenger.size() > 0) {
            ArrayList<Passenger> toGetIn = buildingWaitQueue.getPassenger(dir,
                    nowFloor, size - inElevatorPassenger.size());
            for (Passenger passenger : toGetIn) {
                TimableOutput.println(String.format("IN-%d-%c-%d-%d",
                        passenger.getId(), building, nowFloor, id));
            }
            inElevatorPassenger.addAll(toGetIn);
            if (inElevatorPassenger.size() > 0) {
                dir = inElevatorPassenger.get(0).getDir();
            }
        }
    }

    public void close() throws InterruptedException {
        sleep(closeTime);
        getIn();
        TimableOutput.println(String.format("CLOSE-%c-%d-%d", building, nowFloor, id));
    }

    public boolean hasArrivedPassenger() {
        for (Passenger passenger : inElevatorPassenger) {
            if (passenger.getToFloor() == nowFloor) {
                return true;
            }
        }
        return false;
    }
}
