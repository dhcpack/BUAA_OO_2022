import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import java.io.IOException;
import java.util.ArrayList;

public class Input extends Thread {
    private final ArrayList<RequestQueue> waitQueues;

    public Input(ArrayList<RequestQueue> waitQueues) {
        this.waitQueues = waitQueues;
        for (int i = 0; i < 10; i++) {
            RequestQueue waitQueue = new RequestQueue();
            this.waitQueues.add(waitQueue);
        }
    }

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (RequestQueue queue : waitQueues) {
                    queue.setEnd(true);
                }
                break;
            }
            else {
                if (request instanceof PersonRequest) {
                    PersonRequest personReq = (PersonRequest) request;
                    int index;
                    if (personReq.getFromFloor() != personReq.getToFloor()) {
                        index = personReq.getFromBuilding() - 'A';
                    } else {
                        index = personReq.getFromFloor() + 4;
                    }
                    waitQueues.get(index).addRequest(personReq);
                }
                else if (request instanceof ElevatorRequest) {
                    ElevatorRequest elevatorReq = (ElevatorRequest) request;
                    RequestQueue waitQueue;
                    if (elevatorReq.getType().equals("building")) {
                        waitQueue = waitQueues.get(elevatorReq.getBuilding() - 'A');
                        Elevator elevator = new Elevator(
                                elevatorReq.getElevatorId(), elevatorReq.getBuilding(), waitQueue);
                        elevator.start();

                    } else {
                        waitQueue = waitQueues.get(elevatorReq.getFloor() + 4);
                        Ring ring = new Ring(
                                elevatorReq.getElevatorId(), elevatorReq.getFloor(), waitQueue);
                        ring.start();
                    }

                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
