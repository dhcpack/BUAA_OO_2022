import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/// TODO: 2022/3/31 在和mainRequest方向不同时不开门 ok

public class Elevator implements Runnable {
    private static final int UP = 1;
    private static final int DOWN = -1;

    private final int id;
    private int forward;
    private int currentFloor;
    private PersonRequest mainRequest;
    private RequestTransfer requestTransfer;
    private ArrayList<PersonRequest> requestArrayList;
    private ArrayList<PersonRequest> peopleOnElevator;
    private boolean mainOnElevator;
    private static final int CAPACITY = 5;

    public Elevator(int id, RequestTransfer requestTransfer) {
        this.id = id;
        this.requestTransfer = requestTransfer;
        currentFloor = 1;
        mainRequest = null;
        peopleOnElevator = new ArrayList<>();
        requestArrayList = new ArrayList<>();
        mainOnElevator = false;
    }

    public RequestTransfer getRequestTransfer() {
        return requestTransfer;
    }

    @Override
    public void run() {
        strategyAls();
    }

    private void strategyAls() {
        while (true) {
            if (requestArrayList.size() + peopleOnElevator.size() == 0 && requestTransfer.isEnd()) {
                // 可能此时已经end了
                // System.out.println(Thread.currentThread().getName() + " close");
                return;
            } else if (requestArrayList.size() + peopleOnElevator.size() == 0) {
                requestArrayList.add(requestTransfer.outputRequest());  // 这里必然调用wait()方法，
                if (requestTransfer.isEnd()) {  // 可能等一会等到了end
                    // System.out.println(Thread.currentThread().getName() + " close");
                    return;
                }
            }
            refreshRequest();
            getMainRequest();
            // System.out.println(mainRequest.toString());
            if (!mainOnElevator) {
                move(mainRequest.getFromFloor());
                mainOnElevator = true;
            }
            move(mainRequest.getToFloor());
            mainOnElevator = false;
        }
    }

    private void refreshRequest() {
        while (requestTransfer.hasRequest()) {
            requestArrayList.add(requestTransfer.outputRequest());
        }
    }

    private void getMainRequest() {
        if (peopleOnElevator.size() == 0) {
            mainRequest = requestArrayList.get(0);
            mainOnElevator = false;
        } else {
            int index = 0;
            int minLength = 11;
            for (int i = 0; i < peopleOnElevator.size(); i++) {
                int curLength = Math.abs(peopleOnElevator.get(i).getToFloor() - currentFloor);
                if (curLength < minLength) {
                    index = i;
                    minLength = curLength;
                }
            }
            mainRequest = peopleOnElevator.remove(index);
            mainOnElevator = true;
        }
    }

    private void move(int destination) {
        if (destination > currentFloor) {
            forward = UP;
        } else if (destination < currentFloor) {
            forward = DOWN;
        } else {
            opOnNewFloor();
            return;
        }
        do {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentFloor += forward;
            Output.arrive(currentFloor, id);
            opOnNewFloor();
        } while (currentFloor != destination);
    }

    private void opOnNewFloor() {
        refreshRequest();
        boolean hasOut = hasOut();
        if (hasOut || hasIn(hasOut)) {
            Output.open(currentFloor, id);
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshRequest();
            getOut();
            getIn();
            Output.close(currentFloor, id);
        }

    }

    private boolean hasIn(boolean hasOut) {
        // check for in
        if (!mainOnElevator && mainRequest.getFromFloor() == currentFloor) {
            return true;  // main may get into elevator on this level
        }
        if (peopleOnElevator.size() == CAPACITY && !hasOut) {
            return false;
        }
        for (PersonRequest personRequest : requestArrayList) {
            if (personRequest.getFromFloor() == currentFloor &&
                    ((personRequest.getToFloor() - currentFloor > 0 && forward == UP) ||
                            (personRequest.getToFloor() - currentFloor < 0 && forward == DOWN))) {
                return true;
            }
        }
        return false;

    }

    private boolean hasOut() {
        // check for out
        if (mainOnElevator && mainRequest.getToFloor() == currentFloor) {
            return true;
        }
        for (PersonRequest personRequest : peopleOnElevator) {
            if (personRequest.getToFloor() == currentFloor) {
                return true;
            }
        }
        return false;
    }

    private void getOut() {
        // get out
        ArrayList<PersonRequest> newPeopleOnElevator = new ArrayList<>();
        if (mainOnElevator && mainRequest.getToFloor() == currentFloor) {
            Output.out(mainRequest.getPersonId(), currentFloor, id);
            mainOnElevator = false;
            mainRequest = null;
        }
        for (PersonRequest personRequest : peopleOnElevator) {
            if (personRequest.getToFloor() == currentFloor) {
                Output.out(personRequest.getPersonId(), currentFloor, id);
            } else {
                newPeopleOnElevator.add(personRequest);
            }
        }
        peopleOnElevator = newPeopleOnElevator;
    }

    private void getIn() {
        ArrayList<PersonRequest> newRequestArrayList = new ArrayList<>();
        for (PersonRequest personRequest : requestArrayList) {
            if (!(mainRequest == null) && mainRequest.equals(
                    personRequest) && personRequest.getFromFloor() == currentFloor) {
                Output.in(personRequest.getPersonId(), currentFloor, id);
                mainOnElevator = true;
                if (mainRequest.getToFloor() > currentFloor) {
                    forward = UP;
                } else {
                    forward = DOWN;
                }
            } else if (peopleOnElevator.size() == CAPACITY) {
                continue;
            } else if (personRequest.getFromFloor() == currentFloor &&
                    ((personRequest.getToFloor() - currentFloor > 0 && forward == UP) ||
                            (personRequest.getToFloor() - currentFloor < 0 && forward == DOWN) ||
                            (mainRequest == null))) {
                Output.in(personRequest.getPersonId(), currentFloor, id);
                peopleOnElevator.add(personRequest);
            } else {
                newRequestArrayList.add(personRequest);
            }
        }
        requestArrayList = newRequestArrayList;
    }
}
