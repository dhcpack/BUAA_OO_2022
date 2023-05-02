import java.util.ArrayList;
import java.util.function.Predicate;

public class Elevator implements Runnable {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;
    private static final int CAPACITY = 6;

    private final int elevatorId;
    private final String type;
    private int direction = UP;
    private int currentFloor;
    private int currentBuilding;

    private RequestHandler requestHandler;
    private ArrayList<MyPersonRequest> peopleOnElevator = new ArrayList<>();
    private ArrayList<MyPersonRequest> waitingQueue = new ArrayList<>();

    public Elevator(int elevatorId, String type, int building, int floor,
                    RequestHandler requestHandler) {
        this.elevatorId = elevatorId;
        this.type = type;
        this.currentBuilding = building;
        this.currentFloor = floor;
        this.requestHandler = requestHandler;
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

    public ArrayList<MyPersonRequest> getWaitingQueue() {
        return waitingQueue;
    }

    public int numOfRequests() {
        return peopleOnElevator.size() + waitingQueue.size();
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
                    Thread.sleep(400);
                } else {
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goToNextPlace();
            Output.arrive(currentBuilding, currentFloor, elevatorId);
            changeDirt = opOnNewPlace();
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
                    (personRequest.getFromBuilding() != currentBuilding ||
                            personRequest.getFromFloor() != currentFloor)) {
                return false;
            }
        }
        return true;
    }

    private boolean allowIn(MyPersonRequest personRequest) {
        if (!(personRequest.getFromFloor() == currentFloor &&
                personRequest.getFromBuilding() == currentBuilding)) {
            return false;
        }
        if (personRequest.isSameEndDirection(direction, currentFloor, currentBuilding)) {
            return true;
        }
        return false;
    }

    private boolean hasOut() {
        // check for out
        for (MyPersonRequest personRequest : peopleOnElevator) {
            if (personRequest.getToFloor() == currentFloor &&
                    personRequest.getToBuilding() == currentBuilding) {
                return true;
            }
        }
        return false;
    }

    private boolean hasIn(boolean hasOut) {
        // check for in
        if (peopleOnElevator.size() == CAPACITY && !hasOut) {
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
                if (personRequest.getToFloor() == currentFloor &&
                        personRequest.getToBuilding() == currentBuilding) {
                    Output.out(personRequest.getPersonId(), currentBuilding, currentFloor,
                            elevatorId);
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