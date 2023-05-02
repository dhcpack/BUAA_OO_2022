import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class Scheduler {
    // 纵向请求，一座一个RequestTransfer
    private final ArrayList<RequestHandler> buildingRequestHandler = new ArrayList<>();
    // 横向请求，一层一个RequestTransfer
    private final ArrayList<RequestHandler> floorRequestHandler = new ArrayList<>();
    // 记录每层的floor类型的电梯
    private final ArrayList<ArrayList<Elevator>> floorElevators = new ArrayList<>();
    // 记录所有请求
    private final ArrayList<MyPersonRequest> personRequests = new ArrayList<>();
    private final HashSet<Thread> threads = new HashSet<>();
    private boolean isEnd = false;

    public Scheduler() {
        for (int i = 0; i < 5; i++) {
            buildingRequestHandler.add(new RequestHandler("building"));
        }
        for (int i = 0; i < 10; i++) {
            floorRequestHandler.add(new RequestHandler("floor"));
            floorElevators.add(new ArrayList<>());
        }
        for (int i = 0; i < 5; i++) {
            Elevator elevator = new Elevator(i + 1, "building", i, 1, 8, 0.6, 0,
                    buildingRequestHandler.get(i), this);
            Thread thread = new Thread(elevator);
            threads.add(thread);
            thread.start();
        }
        Elevator elevator = new Elevator(6, "floor", 0, 1, 8, 0.6, 31,
                floorRequestHandler.get(0), this);
        floorElevators.get(0).add(elevator);
        Thread thread = new Thread(elevator);
        threads.add(thread);
        thread.start();
    }

    public synchronized void setEnd() {
        isEnd = true;
        isEnd();
    }

    public void isEnd() {
        if (!(isEnd && personRequests.size() == 0)) {
            return;
        }
        for (RequestHandler requestHandler : buildingRequestHandler) {
            requestHandler.setEnd();
        }
        for (RequestHandler requestHandler : floorRequestHandler) {
            requestHandler.setEnd();
        }
    }

    public synchronized void dealPersonRequest(MyPersonRequest personRequest) {
        personRequests.add(personRequest);
        personRequest.setFloorElevator(floorElevators);
        settlePersonTrips(personRequest);
    }

    public void settlePersonTrips(MyPersonRequest personRequest) {
        personRequest.getOneTrip();
        if (personRequest.getCurrentType().equals("building")) {  // 上building类型电梯
            buildingRequestHandler.get(personRequest.getCurrentStartBuilding())
                    .inputRequest(personRequest);
        } else {  // 上floor类型电梯
            floorRequestHandler.get(personRequest.getCurrentStartFloor() - 1).
                    inputRequest(personRequest);
        }
    }

    public synchronized void finishPersonRequest(MyPersonRequest personRequest) {
        personRequests.remove(personRequest);
        isEnd();
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
                elevatorRequest.getFloor(), elevatorRequest.getCapacity(),
                elevatorRequest.getSpeed(), elevatorRequest.getSwitchInfo(), requestHandler, this);
        if (elevatorRequest.getType().equals("floor")) {
            floorElevators.get(elevatorRequest.getFloor() - 1).add(newElevator);
        }
        Thread newThread = new Thread(newElevator);
        threads.add(newThread);
        newThread.start();
    }

    public HashSet<Thread> getAllThreads() {
        return threads;
    }
}
