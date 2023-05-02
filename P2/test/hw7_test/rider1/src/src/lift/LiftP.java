package src.lift;

import src.enums.BuildingEnum;
import src.enums.DirectionEnum;
import src.io.OutputHandler;
import src.reqhandler.MyPersonRequest;
import src.reqhandler.RequestQueue;
import src.singleton.Counter;
import src.singleton.LiftData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static src.enums.BuildingEnum.A;
import static src.enums.BuildingEnum.find;
import static src.enums.BuildingEnum.getNextBuilding;
import static src.enums.BuildingEnum.needSwitchDir;
import static src.enums.BuildingEnum.next;
import static src.enums.DirectionEnum.DOWN;
import static src.enums.DirectionEnum.UP;

public class LiftP extends Thread {

    private final ArrayBlockingQueue<MyPersonRequest> insideQueue;
    private final RequestQueue outsideQueue;

    private final RequestQueue inputQueue =
            LiftData.fetch().inputQueue();

    private BuildingEnum building;
    private final int floor;
    private final int moveDur;
    private final int openDur;
    private final int closeDur;
    private final int maxNum;
    private final int id;
    private final int accessibility;

    private DirectionEnum direction;
    private src.lift.State state;

    public LiftP(int floor, RequestQueue outsideQueue, int id,
                 double speed, int cap, int accessibility) {
        this.floor = floor;
        this.outsideQueue = outsideQueue;
        this.insideQueue = new ArrayBlockingQueue<>(cap);
        this.state = new StateWaiting();
        this.accessibility = accessibility;

        Random random = new Random();
        this.direction = random.nextInt(2) == 1 ? UP : DOWN;

        this.moveDur = speed < 0.3 ? 200 :
                speed > 0.5 ? 600 : 400;

        this.openDur = 200;
        this.closeDur = 200;
        this.maxNum = cap;
        this.building = A;
        this.id = id;
    }

    public int getAccessibility() {
        return accessibility;
    }

    public int getFloor() {
        return floor;
    }

    public int getMoveDur() {
        return moveDur;
    }

    public int getInsideNum() {
        return insideQueue.size();
    }

    public int getOutsideNum() {
        return outsideQueue.size();
    }

    public int getIdent() {
        return id;
    }

    private void switchDir() {
        direction = direction == UP ? DOWN : UP;
    }

    @Override
    public void run() {
        state.behave();
    }

    class StateWaiting implements src.lift.State {
        @Override
        public void behave() {
            while (true) {
                boolean move = (!insideQueue.isEmpty()) || (!outsideQueue.isEmpty());
                OutputHandler.println("move: " + move, true);
                if (insideQueue.isEmpty() && outsideQueue.isEmpty() &&
                        outsideQueue.isReachEnd()) {
                    return;
                }
                if (!move) {
                    try {
                        synchronized (outsideQueue) {
                            outsideQueue.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    synchronized (outsideQueue) {
                        outsideQueue.notifyAll();
                    }
                    OutputHandler.println("lift " + id + " switched to MOVING", true);
                    state = new LiftP.StateMoving();
                    state.behave();
                    return;
                }
            }
        }
    }

    class StateMoving implements src.lift.State {
        @Override
        public void behave() {
            BuildingEnum startBuilding = building;
            BuildingEnum nextBuilding = nextBuilding();
            OutputHandler.println("dest = " + nextBuilding, true);
            OutputHandler.println("start = " + startBuilding, true);
            if (needSwitchDir(startBuilding, nextBuilding, direction, accessibility)) {
                switchDir();
            }
            while (true) {
                if (building == nextBuilding) {
                    if (building != startBuilding) {
                        OutputHandler.println(String.format("ARRIVE-%s-%d-%d",
                                building, floor, id), false);
                    }
                    synchronized (outsideQueue) {
                        outsideQueue.notifyAll();
                    }
                    state = new StateOpen();
                    state.behave();
                    return;
                } else {
                    try {
                        sleep(moveDur);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    building = next(building, direction);
                    OutputHandler.println(String.format("ARRIVE-%s-%d-%d",
                            building, floor, id), false);
                    if (building == nextBuilding) {
                        state = new StateOpen();
                        state.behave();
                        return;
                    }
                }
            }
        }

        private BuildingEnum nextBuilding() {
            HashMap<BuildingEnum, Boolean> checkTable = new HashMap<>();
            for (BuildingEnum b : BuildingEnum.values()) {
                checkTable.put(b, false);
            }
            for (MyPersonRequest req : outsideQueue.getRequestQueue()) {
                BuildingEnum b = find(req.getFromBuilding() - 'A');
                if (!checkTable.get(b)) {
                    checkTable.computeIfPresent(b, (k, v) -> v = true);
                }
            }
            for (MyPersonRequest req : insideQueue) {
                BuildingEnum b = find(req.getToBuilding() - 'A');
                if (!checkTable.get(b)) {
                    checkTable.computeIfPresent(b, (k, v) -> v = true);
                }
            }
            OutputHandler.println(checkTable.toString(), true);
            BuildingEnum t = building;
            while (true) {
                t = getNextBuilding(t, direction, accessibility);
                //OutputHandler.println("calc nextBuilding: " + t,true);
                if (checkTable.get(t)) {
                    return t;
                }
            }
        }
    }

    class StateOpen implements src.lift.State {
        @Override
        public void behave() {
            OutputHandler.println(String.format("OPEN-%s-%d-%d",
                    building, floor, id), false);
            if (!insideQueue.isEmpty()) {
                Iterator<MyPersonRequest> iter = insideQueue.iterator();
                while (iter.hasNext()) {
                    MyPersonRequest req = iter.next();
                    if (find(req.getToBuilding() - 'A') == building) {
                        iter.remove();
                        OutputHandler.println(String.format("OUT-%d-%s-%d-%d",
                                req.getPersonId(), req.getToBuilding(),
                                req.getToFloor(), id), false);
                        req.setSecondShifted();
                        if (req.getToFloor() != floor) {
                            inputQueue.addRequest(req);
                        } else {
                            synchronized (inputQueue) {
                                inputQueue.notifyAll();
                            }
                            Counter.fetch().releaseRequest(req.getPersonId());
                        }
                    }
                }
            }
            try {
                sleep(openDur);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            state = new StateClose();
            state.behave();
        }
    }

    class StateClose implements src.lift.State {
        @Override
        public void behave() {
            if (!outsideQueue.isEmpty()) {
                Iterator<MyPersonRequest> iter = outsideQueue.getRequestQueue().iterator();
                while (iter.hasNext()) {
                    MyPersonRequest req = iter.next();
                    if (find(req.getFromBuilding() - 'A') == building &&
                            insideQueue.remainingCapacity() > 0) {
                        insideQueue.add(req);
                        iter.remove();
                        OutputHandler.println(String.format("IN-%d-%s-%d-%d",
                                req.getPersonId(), req.getFromBuilding(),
                                req.getFromFloor(), id), false);
                    }
                }
            }
            try {
                sleep(closeDur);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            OutputHandler.println(String.format("CLOSE-%s-%d-%d", building, floor, id), false);
            state = new StateWaiting();
            state.behave();
        }
    }
}
