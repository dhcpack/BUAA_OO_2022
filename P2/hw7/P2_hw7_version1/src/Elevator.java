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
    private int direction = UP;
    private int currentFloor;
    private int currentBuilding;

    private final Scheduler scheduler;
    private final RequestHandler requestHandler;
    private final ArrayList<MyPersonRequest> peopleOnElevator = new ArrayList<>();
    private final ArrayList<MyPersonRequest> waitingQueue = new ArrayList<>();

    public Elevator(int elevatorId, String type, int building, int floor, int capacity,
                    double speed, int reachPlace, RequestHandler requestHandler,
                    Scheduler scheduler) {
        this.elevatorId = elevatorId;
        this.type = type;
        this.currentBuilding = building;
        this.currentFloor = floor;
        this.capacity = capacity;
        this.speed = (long) (1000 * speed);
        this.reachPlace = reachPlace;
        this.requestHandler = requestHandler;
        this.scheduler = scheduler;
    }

    public String getType() {
        return type;
    }

    public long getSpeed() {
        return speed;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCurrentBuilding() {
        return currentBuilding;
    }

    public int getDirection() {
        return direction;
    }

    public boolean canReach(int startBuilding, int toBuilding) {
        // ((M >> (P -'A')) & 1) + ((M >> (Q -'A')) & 1) == 2
        return ((reachPlace >> startBuilding) & 1) + ((reachPlace >> toBuilding) & 1) == 2;
    }

    public ArrayList<MyPersonRequest> getWaitingQueue() {
        return waitingQueue;
    }

    public int numOfCurrentDirectionRequests() {
        int cnt = 0;
        for (MyPersonRequest personRequest : waitingQueue) {
            if (personRequest.isSameEndDirection(direction, personRequest.getCurrentStartFloor(),
                    personRequest.getCurrentStartBuilding()) && personRequest.isSameStartDirection(
                    direction, currentFloor, currentBuilding)) {
                cnt++;
            }
        }
        return peopleOnElevator.size() + cnt;
    }

    public int numOfNextDirectionRequests() {
        int cnt = 0;
        for (MyPersonRequest personRequest : waitingQueue) {
            if (personRequest.isSameEndDirection(direction, personRequest.getCurrentStartFloor(),
                    personRequest.getCurrentStartBuilding()) && personRequest.isSameStartDirection(
                    direction, currentFloor, currentBuilding)) {
                cnt++;
            }
        }
        return waitingQueue.size() - cnt;
    }

    @Override
    public void run() {
        strategyLook();
    }

    private boolean isEnd() {  // true is end
        refreshRequest();
        return peopleOnElevator.size() == 0 && waitingQueue.size() == 0 && requestHandler.isEnd();
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
            try {
                if (type.equals("building")) {
                    Thread.sleep(speed);
                } else {
                    Thread.sleep(speed);
                }
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
        refreshRequest();
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
            changeDirectionRefresh();
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
        if (peopleOnElevator.size() != 0) {
            return false;
        }
        for (MyPersonRequest personRequest : waitingQueue) {
            if (personRequest.isSameStartDirection(direction, currentFloor, currentBuilding) &&
                    (personRequest.getCurrentStartBuilding() != currentBuilding ||
                            personRequest.getCurrentStartFloor() != currentFloor)) {
                return false;
            }
        }
        return true;
    }

    private boolean allowIn(MyPersonRequest personRequest) {
        if (!(personRequest.getCurrentStartFloor() == currentFloor &&
                personRequest.getCurrentStartBuilding() == currentBuilding)) {
            return false;
        }
        if (personRequest.isSameEndDirection(direction, currentFloor, currentBuilding)) {
            return true;
        }
        return false;
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
        for (MyPersonRequest personRequest : waitingQueue) {
            if (allowIn(personRequest)) {
                return true;
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
        waitingQueue.removeIf(new Predicate<MyPersonRequest>() {
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

    private void refreshRequest() {
        requestHandler.arrangeRequests(this, false);
    }

    private void changeDirectionRefresh() {
        requestHandler.arrangeRequests(this, true);
    }

    private boolean open() {
        Output.open(currentBuilding, currentFloor, elevatorId);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        refreshRequest();
        return true;
    }
}