import java.util.ArrayList;
import java.util.function.Predicate;

public class Elevator implements Runnable {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    private final int elevatorId;
    private final String type;
    private final int capacity;
    private final long speed;
    private final int reachPlace;
    private boolean schedulerEnd;
    private int direction;
    private int currentFloor;
    private int currentBuilding;

    private final Scheduler scheduler;
    private final ArrayList<MyPersonRequest> requestHandler;
    private final ArrayList<MyPersonRequest> peopleOnElevator = new ArrayList<>();

    public Elevator(int elevatorId, String type, int building, int floor, int capacity,
                    double speed, int reachPlace, ArrayList<MyPersonRequest> requestHandler,
                    Scheduler scheduler, int direction) {
        this.elevatorId = elevatorId;
        this.type = type;
        this.currentBuilding = building;
        this.currentFloor = floor;
        this.capacity = capacity;
        this.speed = (long) (1000 * speed);
        this.reachPlace = reachPlace;
        this.requestHandler = requestHandler;
        this.scheduler = scheduler;
        this.direction = direction;
    }

    public long getSpeed() {
        return speed;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean canReach(int startBuilding, int toBuilding) {
        // ((M >> (P -'A')) & 1) + ((M >> (Q -'A')) & 1) == 2
        return ((reachPlace >> startBuilding) & 1) + ((reachPlace >> toBuilding) & 1) == 2;
    }

    @Override
    public void run() {
        strategyLook();
    }

    public void setSchedulerEnd() {
        synchronized (requestHandler) {
            schedulerEnd = true;
            requestHandler.notifyAll();
        }
    }

    private boolean isEnd() {  // true is end
        synchronized (requestHandler) {
            if (peopleOnElevator.size() == 0 && requestHandler.size() == 0 && !schedulerEnd) {
                try {
                    requestHandler.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return peopleOnElevator.size() == 0 && requestHandler.size() == 0 && schedulerEnd;
        }
    }

    private void strategyLook() {
        while (!isEnd()) {
            move();
        }
    }

    private void goToNextPlace() {
        if (type.equals("building")) {
            currentFloor += direction;
        } else {
            currentBuilding = (currentBuilding + direction + 5) % 5;
        }
    }

    private void move() {
        boolean changeDirt = opOnNewPlace();
        while (!changeDirt) {
            if (type.equals("floor") &&
                    requestHandler.size() == 0 && peopleOnElevator.size() == 0) {
                break;
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goToNextPlace();
            Output.arrive(currentBuilding, currentFloor, elevatorId);
            if (allowOpen()) {
                changeDirt = opOnNewPlace();
            }
        }
    }

    private boolean opOnNewPlace() {
        boolean hasOut = hasOut();
        boolean changeDirt = false;
        boolean isOpen = false;
        if (hasOut || hasIn(hasOut)) {
            isOpen = open();
            getOut();
            getIn();
        }
        if (changeDirection()) {
            direction = -direction;
            changeDirt = true;
            if (hasIn(false)) {
                if (!isOpen) {
                    isOpen = open();
                }
                getIn();
            }
        }
        if (isOpen) {
            Output.close(currentBuilding, currentFloor, elevatorId);
        }
        return changeDirt;
    }

    private boolean changeDirection() {
        if (peopleOnElevator.size() != 0 || type.equals("floor")) {
            return false;
        }
        synchronized (requestHandler) {
            for (MyPersonRequest personRequest : requestHandler) {
                if (personRequest.isSameStartDirection(direction, currentFloor, currentBuilding)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean allowOpen() {
        if (type.equals("building")) {
            return true;
        }
        return ((reachPlace >> currentBuilding) & 1) == 1;
    }

    private boolean hasOut() {
        // check for out
        for (MyPersonRequest personRequest : peopleOnElevator) {
            if (personRequest.getCurrentEndFloor() == currentFloor &&
                    personRequest.getCurrentEndBuilding() == currentBuilding) {
                return true;
            }
        }
        return false;
    }

    private boolean hasIn(boolean hasOut) {
        // check for in
        if (peopleOnElevator.size() == capacity && !hasOut) {
            return false;
        }
        synchronized (requestHandler) {
            for (MyPersonRequest personRequest : requestHandler) {
                if (allowIn(personRequest)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getOut() {
        // get out
        peopleOnElevator.removeIf(new Predicate<MyPersonRequest>() {
            @Override
            public boolean test(MyPersonRequest personRequest) {
                if (personRequest.getCurrentEndFloor() == currentFloor &&
                        personRequest.getCurrentEndBuilding() == currentBuilding) {
                    Output.out(personRequest.getPersonId(), currentBuilding, currentFloor,
                            elevatorId);
                    if (personRequest.isDestination(currentFloor, currentBuilding)) {
                        scheduler.finishPersonRequest(personRequest);
                    } else {
                        scheduler.settlePersonTrips(personRequest);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getIn() {
        synchronized (requestHandler) {
            requestHandler.removeIf(new Predicate<MyPersonRequest>() {
                @Override
                public boolean test(MyPersonRequest personRequest) {
                    if (peopleOnElevator.size() == capacity) {
                        return false;
                    }
                    if (allowIn(personRequest)) {
                        peopleOnElevator.add(personRequest);
                        Output.in(personRequest.getPersonId(), currentBuilding, currentFloor,
                                elevatorId);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private boolean allowIn(MyPersonRequest personRequest) {  // allow in 不检查电梯容量
        if (!(personRequest.getCurrentStartFloor() == currentFloor &&
                personRequest.getCurrentStartBuilding() == currentBuilding)) {
            return false;
        }
        if (!personRequest.getCurrentType().equals(type)) {
            return false;
        }
        if (type.equals("floor")) {
            return canReach(personRequest.getCurrentStartBuilding(),
                    personRequest.getCurrentEndBuilding());  // floor类型电梯，能到达则进
        }
        // building类型电梯需要方向相同
        return personRequest.isSameEndDirection(direction, currentFloor, currentBuilding);
    }

    private boolean open() {
        Output.open(currentBuilding, currentFloor, elevatorId);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}