import java.util.ArrayList;
import java.util.stream.Collectors;

public class LookElevator extends Elevator {
    private LookDirection lookDirection = LookDirection.Up;

    public LookElevator(RequestQueue requestQueue, int capacity, Building building, int id) {
        super(requestQueue, capacity, building, id, 1);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this.getRequestQueue()) {
                if (this.getRequestQueue().isFinish() && this.getCarrying().isEmpty() &&
                        this.getElevatorState() == ElevatorState.Closed) {
                    return;
                }

                if (this.getElevatorState() == ElevatorState.Closed &&
                        this.getCarrying().isEmpty() && this.getRequestQueue().isEmpty()) {
                    //System.out.println("waiting..." + this.getBuilding());
                    this.getRequestQueue().watchForRequest();
                    //System.out.println("notified...in" + this.getBuilding());
                }
            }
            ArrayList<MyRequest> requests =
                    this.getRequestQueue().getRequest();

            int directionIndicator; // to simplify the implementation of look algorithm
            // only respond to request to our current direction
            if (this.lookDirection == LookDirection.Up) {
                directionIndicator = 1;
            } else {
                directionIndicator = -1;
            }
            //requests which can be responded in this pass
            ArrayList<MyRequest> respondedRequest =
                    (ArrayList<MyRequest>) requests.stream()
                            .filter(request -> request.getFromFloor() == this.getFloor() &&
                                    (request.getToFloor() - request.getFromFloor())
                                            * directionIndicator > 0
                            )
                            .collect(Collectors.toList());

            switch (this.getElevatorState()) {
                case Closed:
                    if (handleClosed(requests, respondedRequest)) {
                        return;
                    }
                    break;
                case Opening:
                    if (handleOpening(respondedRequest)) {
                        return;
                    }
                    break;
                case Closing:
                    if (handleClosing(respondedRequest)) {
                        return;
                    }
                    break;
                case Opened:
                    break;
                case MovingUp:
                case MovingDown:
                    if (handleMoving()) {
                        return;
                    }

                    break;
                default:
                    assert false;
            }
        }

    }

    private boolean handleMoving() {
        if (System.currentTimeMillis() - this.getLastInvokeTime() >
                Elevator.MOVING_INTERVAL_VERTICAL) {
            if (this.getElevatorState() == ElevatorState.MovingUp) {
                this.upFloor();
            } else {
                this.downFloor();
            }
        } else {
            long t = Elevator.MOVING_INTERVAL_VERTICAL -
                    (System.currentTimeMillis() - this.getLastInvokeTime());
            if (t > 0) {
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean handleClosed(
            ArrayList<MyRequest> requests,
            ArrayList<MyRequest> respondedRequest
    ) {
        // 有上的或者有下的
        if ((!respondedRequest.isEmpty() && this.getCarrying().size() <= MAX_CARRY - 1) || // 有上且可以上
                this.getCarrying()
                        .stream()
                        .anyMatch(request -> request.getToFloor() == this.getFloor()
                        )) { //有下
            this.openDoor();
            for (MyRequest req :
                    respondedRequest) {
                if (this.getCarrying().size() > MAX_CARRY - 1) {
                    break;
                }
                this.invitePerson(req);
            }
            this.setLastInvokeTime(System.currentTimeMillis());
        } else if (this.lookDirection == LookDirection.Up && (!this.getCarrying().isEmpty() ||
                requests.stream().anyMatch(request -> request.getFromFloor() > this.getFloor()))) {
            this.moveUp();
        } else if (this.lookDirection == LookDirection.Down && (!this.getCarrying().isEmpty() ||
                requests.stream().anyMatch(request -> request.getFromFloor() < this.getFloor()))) {
            this.moveDown();
        } else if (!requests.isEmpty()) {
            // look algorithm: 没有更多请求就转向
            if (this.lookDirection == LookDirection.Up) {
                this.lookDirection = LookDirection.Down;
            } else {
                this.lookDirection = LookDirection.Up;
            }
        }
        return false;
    }

    private boolean handleOpening(ArrayList<MyRequest> respondedRequest) {
        // if there are any new request, take them
        for (MyRequest req :
                respondedRequest) {
            if (this.getCarrying().size() > MAX_CARRY - 1) {
                break;
            }
            this.invitePerson(req);
        }

        //drop person arrived
        ArrayList<MyRequest> finishedRequest = (ArrayList<MyRequest>) this.getCarrying()
                .stream()
                .filter(
                        request -> request.getToFloor() == this.getFloor()
                ).collect(Collectors.toList());

        for (MyRequest req :
                finishedRequest) {
            this.dropPerson(req);
        }

        if (System.currentTimeMillis() - this.getLastInvokeTime() > Elevator.OPEN_DOOR_INTERVAL) {
            // door opened
            this.beginCloseDoor();
        } else {
            long t = Elevator.OPEN_DOOR_INTERVAL -
                    (System.currentTimeMillis() - this.getLastInvokeTime());
            if (t > 0) {
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean handleClosing(ArrayList<MyRequest> respondedRequest) {
        // if there are any new request, take them
        if (!respondedRequest.isEmpty()) {
            for (MyRequest req :
                    respondedRequest) {
                if (this.getCarrying().size() > MAX_CARRY - 1) {
                    break;
                }
                this.invitePerson(req);
            }
        }
        if (System.currentTimeMillis() - this.getLastInvokeTime() > Elevator.CLOSE_DOOR_INTERVAL) {
            // door closed
            this.endCloseDoor();
        } else {
            long t = Elevator.CLOSE_DOOR_INTERVAL -
                    (System.currentTimeMillis() - this.getLastInvokeTime());
            if (t > 0) {
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    return true;
                }
            }
        }
        return false;
    }

    enum LookDirection {
        Up,
        Down,
    }
}
