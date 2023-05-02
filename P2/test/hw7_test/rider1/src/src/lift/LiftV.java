package src.lift;

import src.enums.BuildingEnum;
import src.enums.DirectionEnum;
import src.io.OutputHandler;
import src.reqhandler.MyPersonRequest;
import src.reqhandler.RequestQueue;
import src.singleton.Counter;
import src.singleton.LiftData;
import src.strategy.StrategyV;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import static src.enums.DirectionEnum.DOWN;
import static src.enums.DirectionEnum.UP;

public class LiftV extends Thread {

    private final ArrayBlockingQueue<MyPersonRequest> insideQueue;
    private final RequestQueue outsideQueue;

    private final RequestQueue inputQueue =
            LiftData.fetch().inputQueue();

    private final BuildingEnum building;
    private int floor;
    private final int maxFloor;
    private final int minFloor;
    private final int moveDur;
    private final int openDur;
    private final int closeDur;
    private final int maxNum;
    private final StrategyV strategy;
    private final int id;

    private DirectionEnum direction;
    private src.lift.State state;

    public LiftV(int type, RequestQueue serveQueue, int id,
                 double speed, int cap) {

        this.building = BuildingEnum.find(type);
        this.outsideQueue = serveQueue;
        this.insideQueue = new ArrayBlockingQueue<>(cap);
        this.id = id;

        this.moveDur = speed < 0.3 ? 200 :
                speed > 0.5 ? 600 : 400;

        this.maxFloor = 10;
        this.minFloor = 1;
        this.openDur = 200;
        this.closeDur = 200;
        this.maxNum = cap;
        this.floor = 1;
        this.direction = UP;
        this.strategy = new StrategyV(this);
        this.state = new StateWaiting();
    }

    public int getFloor() {
        return floor;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setDirection(DirectionEnum direction) {
        this.direction = direction;
    }

    public int getInsideNum() {
        return insideQueue.size();
    }

    public int getMoveDur() {
        return moveDur;
    }

    @Override
    public void run() {
        state.behave();
    }

    final class StateWaiting implements src.lift.State {
        @Override
        public void behave() {
            while (true) {
                int nextDest = strategy.nextDest(insideQueue, outsideQueue);
                if (insideQueue.isEmpty() && outsideQueue.isEmpty() &&
                        outsideQueue.isReachEnd()) {
                    return;
                }
                if (nextDest == -1) {
                    try {
                        synchronized (outsideQueue) {
                            outsideQueue.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    setDirection(floor > nextDest ? DOWN : UP);
                    synchronized (outsideQueue) {
                        outsideQueue.notifyAll();
                    }
                    state = new StateMoving();
                    state.behave();
                    return;
                }
            }
        }
    }

    final class StateMoving implements src.lift.State {
        @Override
        public void behave() {
            int startFloor = getFloor();
            int nextDest = strategy.nextDest(insideQueue, outsideQueue);
            while (true) {
                if (floor == nextDest) {
                    if (floor != startFloor) {
                        OutputHandler.println(String.format("ARRIVE-%s-%d-%d",
                                building, floor, id),false);
                    }
                    synchronized (outsideQueue) {
                        outsideQueue.notifyAll();
                    }
                    state = new StateOpen();
                    state.behave();
                    return;
                } else {
                    direction = nextDest > floor ? UP : DOWN;
                    try {
                        sleep(moveDur);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    floor += direction == UP ? 1 : -1;
                    OutputHandler.println(String.format("ARRIVE-%s-%d-%d",
                            building, floor, id),false);
                    if (nextDest == floor) {
                        state = new StateOpen();
                        state.behave();
                        return;
                    }
                }
            }
        }
    }

    class StateOpen implements src.lift.State {
        @Override
        public void behave() {
            int nextDest = strategy.nextDest(insideQueue,outsideQueue);
            direction = nextDest > floor ? UP : nextDest < floor ? DOWN : direction;
            OutputHandler.println(String.format("OPEN-%s-%d-%d",
                    building, floor, id),false);
            if (!insideQueue.isEmpty()) {
                Iterator<MyPersonRequest> iter = insideQueue.iterator();
                while (iter.hasNext()) {
                    MyPersonRequest req = iter.next();
                    if (req.getToFloor() == floor) {
                        iter.remove();
                        OutputHandler.println(String.format("OUT-%d-%s-%d-%d",
                                req.getPersonId(), req.getFromBuilding(),
                                req.getToFloor(), id),false);
                        if (!req.isVertical() && !req.isFirstShifted()) {
                            req.setFirstShifted();
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
                    if (req.getFromFloor() == floor &&
                            insideQueue.remainingCapacity() > 0 &&
                            direction == getDir(req)) {
                        insideQueue.add(req);
                        iter.remove();
                        OutputHandler.println(String.format("IN-%d-%s-%d-%d",
                                req.getPersonId(), req.getFromBuilding(),
                                req.getFromFloor(), id),false);
                    }
                }
            }
            try {
                sleep(closeDur);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            OutputHandler.println(String.format("CLOSE-%s-%d-%d", building, floor, id),false);
            state = new StateWaiting();
            state.behave();
        }
    }

    private DirectionEnum getDir(MyPersonRequest req) {
        return req.getFromFloor() > req.getToFloor() ? DOWN : UP;
    }

}
