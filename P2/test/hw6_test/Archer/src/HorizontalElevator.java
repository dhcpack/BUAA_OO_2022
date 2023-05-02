import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 在各座之间移动的电梯，策略是只要有请求就顺时针转，依次完成请求。
 */
public class HorizontalElevator extends Elevator {
    public HorizontalElevator(RequestQueue requestQueue, int capacity, int id, int floor) {
        super(requestQueue, capacity, Building.A, id, floor);
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
                    this.getRequestQueue().watchForRequest();
                }
            }
            ArrayList<MyRequest> requests =
                    this.getRequestQueue().getRequest();

            ArrayList<MyRequest> respondedRequest =
                    (ArrayList<MyRequest>) requests.stream()
                            .filter((req) -> req.getFromBuilding() == this.getBuilding())
                            .collect(Collectors.toList());

            switch (this.getElevatorState()) {
                case Closed:
                    handleClosed(respondedRequest, requests);

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
                case MovingClock:
                case MovingRClock:
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
                Elevator.MOVING_INTERVAL_HORIZONTAL) {
            if (this.getElevatorState() == ElevatorState.MovingClock) {
                this.clockwiseBuilding();
            } else {
                this.counterClockwiseBuilding();
            }
        } else {
            long t = Elevator.MOVING_INTERVAL_HORIZONTAL -
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

    private void handleClosed(
            ArrayList<MyRequest> respondedRequest,
            ArrayList<MyRequest> requests
    ) {
        if ((!respondedRequest.isEmpty() && this.getCarrying().size() <= MAX_CARRY - 1) || // 有上且可以上
                this.getCarrying()
                        .stream()
                        .anyMatch(request -> request.getToBuilding() == this.getBuilding()
                        )) { //有下
            this.openDoor();
            this.setLastInvokeTime(System.currentTimeMillis());
        } else if (!getCarrying().isEmpty() || !requests.isEmpty()) {
            this.moveClockwise();
        }
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
                        request -> request.getToBuilding() == this.getBuilding()
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

}
