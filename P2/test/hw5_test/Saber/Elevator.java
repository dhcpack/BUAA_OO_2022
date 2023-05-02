import com.oocourse.elevator1.PersonRequest;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator implements Runnable {
    private int curFloor;
    private final int openTime = 200;
    private final int closeTime = 200;
    private final int moveTime = 400;
    private int runMode;
    private int id;
    private int maxPeople;
    private Building building;
    private HashSet<PersonRequest> inside;

    public Elevator(int id, Building building) {
        this.id = id;
        this.curFloor = 1;
        this.runMode = 1;
        this.maxPeople = 6;
        this.building = building;
        this.inside = new HashSet<>();
    }

    public boolean isEmpty() {
        return inside.isEmpty();
    }

    public String getInfo() {
        return building.getId() + "-" + curFloor + "-" + id;
    }

    public void openDoor() {
        OutputHandler.print("OPEN-" + getInfo());
        try {
            Thread.sleep(openTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDoor() {
        try {
            Thread.sleep(closeTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OutputHandler.print("CLOSE-" + getInfo());
    }

    public void letOut() {
        for (PersonRequest r : inside) {
            if (r.getToFloor() == curFloor) {
                OutputHandler.print("OUT-" + r.getPersonId() + "-" + getInfo());
            }
        }
        inside.removeIf(r -> r.getToFloor() == curFloor);
    }

    public void letIn() {
        Floor floor = building.getFloor(curFloor);
        int capacity = maxPeople - inside.size();
        LinkedBlockingQueue<PersonRequest> newreqs;
        if (isEmpty() && building.invHere(curFloor, runMode)) {
            runMode = -runMode;
        }
        if (runMode == 1) {
            newreqs = floor.getUpperReqs(capacity);
        } else {
            newreqs = floor.getLowerReqs(capacity);
        }
        for (PersonRequest r : newreqs) {
            OutputHandler.print("IN-" + r.getPersonId() + "-" + getInfo());
        }
        inside.addAll(newreqs);
    }

    public boolean decideStop(int curFloor) {
        for (PersonRequest r : inside) {
            if (r.getToFloor() == curFloor) {
                return true;
            }
        }
        Floor floor = building.getFloor(curFloor);
        if ((runMode == 1 && floor.getUpperCount() > 0) ||
                (runMode == -1 && floor.getLowerCount() > 0)) {
            return true;
        }
        if (isEmpty() && building.invHere(curFloor, runMode)) {
            runMode = -runMode;
            return true;
        }
        return false;
    }

    public boolean setEnded() {
        return building.isEnded() && building.noRequest() && isEmpty();
    }

    @Override
    public void run() {
        int ret = 1;
        while (true) {
            if (isEmpty()) {
                ret = building.getRunMode(curFloor, runMode);
                if (ret != 0) {
                    runMode = ret;
                } else {
                    if (building.invReq(curFloor, runMode)) {
                        runMode = -runMode;
                    }
                }
            }
            if (setEnded()) {
                break;
            }
            if (ret != 0) {
                try {
                    Thread.sleep(moveTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                curFloor += runMode;
                OutputHandler.print("ARRIVE-" + getInfo());
            } else {
                ret = 1;
            }
            if (decideStop(curFloor)) {
                openDoor();
                letOut();
                Thread.yield();
                letIn();
                closeDoor();
            }
        }
    }
}
