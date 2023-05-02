import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.function.Predicate;

import static java.lang.Thread.getDefaultUncaughtExceptionHandler;
import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int CAPACITY = 6;

    private final int elevatorId;
    private int direction = UP;
    private int currentFloor = 1;

    private RequestTransfer requestTransfer;
    private ArrayList<PersonRequest> peopleWaitElevator = new ArrayList<>();
    private ArrayList<PersonRequest> peopleOnElevator = new ArrayList<>();

    public Elevator(int elevatorId, RequestTransfer requestTransfer) {
        this.elevatorId = elevatorId;
        this.requestTransfer = requestTransfer;
    }

    @Override
    public void run() {
        strategyLook();
    }

    private void strategyLook() {
        while (true) {
            if ((peopleWaitElevator.size() + peopleOnElevator.size()) == 0 && requestTransfer.isEnd()) {
                // 可能此时已经end了
                // System.out.println(Thread.currentThread().getName() + " close");
                return;
            } else if (peopleWaitElevator.size() + peopleOnElevator.size() == 0) {
                peopleWaitElevator.add(requestTransfer.outputRequest());
                if (requestTransfer.isEnd()) {  // 可能等一会等到了end
                    // System.out.println(Thread.currentThread().getName() + " close");
                    return;
                }
            }
            move();
        }
    }

    private void move() {
        opOnNewFloor();
        boolean changeDirt = false;
        do {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentFloor += direction;
            Output.arrive(currentFloor, elevatorId);
            changeDirt = opOnNewFloor();
        } while (!changeDirt);
    }

    private boolean changeDirection() {
        for (PersonRequest personRequest : peopleWaitElevator) {
            if ((personRequest.getFromFloor() > currentFloor && direction == UP) || (personRequest.getFromFloor() < currentFloor && direction == DOWN)) {
                return false;
            }
        }
        for (PersonRequest personRequest : peopleOnElevator) {
            if ((personRequest.getToFloor() - currentFloor > 0 && direction == UP) || (personRequest.getToFloor() - currentFloor < 0 && direction == DOWN)) {
                return true;
            }
        }
        return true;
    }

    private void refreshRequest() {
        while (requestTransfer.hasRequest()) {
            peopleWaitElevator.add(requestTransfer.outputRequest());
        }
    }

    private boolean opOnNewFloor() {
        refreshRequest();
        boolean hasOut = hasOut();
        boolean changeDirt = false;
        boolean isOpen = false;
        if (hasOut || hasIn(hasOut)) {
            isOpen = open();
            refreshRequest();
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
            Output.close(currentFloor, elevatorId);
        }
        return changeDirt;
    }

    private boolean open() {
        Output.open(currentFloor, elevatorId);
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean hasOut() {
        // check for out
        for (PersonRequest personRequest : peopleOnElevator) {
            if (personRequest.getToFloor() == currentFloor) {
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
        for (PersonRequest personRequest : peopleWaitElevator) {
            if (personRequest.getFromFloor() == currentFloor && ((personRequest.getToFloor() - currentFloor > 0 && direction == UP) || (personRequest.getToFloor() - currentFloor < 0 && direction == DOWN))) {
                return true;
            }
        }
        return false;
    }

    private void getOut() {
        // get out
        peopleOnElevator.removeIf(new Predicate<PersonRequest>() {
            @Override
            public boolean test(PersonRequest personRequest) {
                if (personRequest.getToFloor() == currentFloor) {
                    Output.out(personRequest.getPersonId(), currentFloor, elevatorId);
                    return true;
                }
                return false;
            }
        });
    }

    private void getIn() {
        peopleWaitElevator.removeIf(new Predicate<PersonRequest>() {
            @Override
            public boolean test(PersonRequest personRequest) {
                if (!(peopleOnElevator.size() == CAPACITY) && personRequest.getFromFloor() == currentFloor) {
                    peopleOnElevator.add(personRequest);
                    Output.in(personRequest.getPersonId(), currentFloor, elevatorId);
                    return true;
                }
                return false;
            }
        });
    }


}
