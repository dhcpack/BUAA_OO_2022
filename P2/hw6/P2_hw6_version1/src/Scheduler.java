import com.oocourse.elevator2.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class Scheduler {
    // 纵向请求，一座一个RequestTransfer
    private ArrayList<RequestHandler> buildingRequestHandler = new ArrayList<>();
    // 横向请求，一层一个RequestTransfer
    private ArrayList<RequestHandler> floorRequestHandler = new ArrayList<>();
    private HashSet<Thread> threads = new HashSet<>();

    public Scheduler() {
        for (int i = 0; i < 5; i++) {
            buildingRequestHandler.add(new RequestHandler());
        }
        for (int i = 0; i < 10; i++) {
            floorRequestHandler.add(new RequestHandler());
        }
        for (int i = 0; i < 5; i++) {
            Elevator elevator = new Elevator(i + 1, "building", i, 1,
                    buildingRequestHandler.get(i));
            Thread thread = new Thread(elevator);
            threads.add(thread);
            thread.start();
        }
    }

    public void setEnd() {
        for (RequestHandler requestHandler : buildingRequestHandler) {
            requestHandler.setEnd();
        }
        for (RequestHandler requestHandler : floorRequestHandler) {
            requestHandler.setEnd();
        }
    }

    public void dealPersonRequest(MyPersonRequest personRequest) {
        if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {  // 上building类型电梯
            buildingRequestHandler.get(personRequest.getFromBuilding())
                    .inputRequest(personRequest);
        } else {  // 上floor类型电梯
            floorRequestHandler.get(personRequest.getFromFloor() - 1).inputRequest(personRequest);
        }

    }

    public void dealElevatorRequest(ElevatorRequest elevatorRequest) {
        RequestHandler requestHandler;
        if (elevatorRequest.getType().equals("building")) {
            requestHandler = buildingRequestHandler.get(elevatorRequest.getBuilding() - 'A');
        } else {
            requestHandler = floorRequestHandler.get(elevatorRequest.getFloor() - 1);
        }
        Elevator newElevator = new Elevator(elevatorRequest.getElevatorId(),
                elevatorRequest.getType(), elevatorRequest.getBuilding() - 'A',
                elevatorRequest.getFloor(), requestHandler);
        Thread newThread = new Thread(newElevator);
        threads.add(newThread);
        newThread.start();
    }

    public HashSet<Thread> getAllThreads() {
        return threads;
    }
}
