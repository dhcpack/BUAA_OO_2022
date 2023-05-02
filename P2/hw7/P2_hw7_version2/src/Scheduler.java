import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class Scheduler {
    // 纵向请求，一座一个RequestTransfer
    private final ArrayList<ArrayList<MyPersonRequest>> buildingRequestHandler = new ArrayList<>();
    // 横向请求，一层一个RequestTransfer
    private final ArrayList<ArrayList<MyPersonRequest>> floorRequestHandler = new ArrayList<>();
    // 记录每层的floor类型的电梯
    private final ArrayList<ArrayList<Elevator>> floorElevators = new ArrayList<>();
    // 记录所有请求
    private final ArrayList<MyPersonRequest> personRequests = new ArrayList<>();
    // 记录所有电梯
    private final ArrayList<Elevator> elevators = new ArrayList<>();
    private final HashSet<Thread> threads = new HashSet<>();
    private boolean isEnd = false;

    public Scheduler() {
        for (int i = 0; i < 5; i++) {
            buildingRequestHandler.add(new ArrayList<>());
        }
        for (int i = 0; i < 10; i++) {
            floorRequestHandler.add(new ArrayList<>());
            floorElevators.add(new ArrayList<>());
        }
        for (int i = 0; i < 5; i++) {
            Elevator elevator = new Elevator(i + 1, "building", i, 1, 8, 0.6, 0,
                    buildingRequestHandler.get(i), this, 1);
            elevators.add(elevator);
            Thread thread = new Thread(elevator);
            threads.add(thread);
            thread.start();
        }
        Elevator elevator = new Elevator(6, "floor", 0, 1, 8, 0.6, 31,
                floorRequestHandler.get(0), this, 1);
        floorElevators.get(0).add(elevator);
        elevators.add(elevator);
        Thread thread = new Thread(elevator);
        threads.add(thread);
        thread.start();
    }

    public synchronized void setEnd() {
        isEnd = true;
        isEnd();
    }

    public void isEnd() {  // 调用isEnd的两个函数都已加锁
        if (!(isEnd && personRequests.size() == 0)) {
            return;
        }
        for (Elevator elevator : elevators) {
            elevator.setSchedulerEnd();
        }
    }

    public synchronized void dealPersonRequest(MyPersonRequest personRequest) {
        personRequests.add(personRequest);
        personRequest.setFloorElevator(floorElevators);
        settlePersonTrips(personRequest);
    }

    public void settlePersonTrips(MyPersonRequest personRequest) {
        personRequest.getOneTrip();
        ArrayList<MyPersonRequest> requestHandler;
        if (personRequest.getCurrentType().equals("building")) {  // 上building类型电梯
            requestHandler = buildingRequestHandler.get(personRequest.getCurrentStartBuilding());
        } else {  // 上floor类型电梯
            requestHandler = floorRequestHandler.get(personRequest.getCurrentStartFloor() - 1);
        }
        synchronized (requestHandler) {
            requestHandler.add(personRequest);
            requestHandler.notifyAll();
        }
    }

    public synchronized void finishPersonRequest(MyPersonRequest personRequest) {
        personRequests.remove(personRequest);
        isEnd();
    }

    public void dealElevatorRequest(ElevatorRequest elevatorRequest) {
        ArrayList<MyPersonRequest> requestHandler;
        int direction = 1;
        if (elevatorRequest.getType().equals("building")) {
            requestHandler = buildingRequestHandler.get(elevatorRequest.getBuilding() - 'A');
        } else {
            requestHandler = floorRequestHandler.get(elevatorRequest.getFloor() - 1);
            if (floorElevators.get(elevatorRequest.getFloor() - 1).size() % 2 == 1) {
                direction = -direction;  // 第二个电梯反向运行
            }
        }
        Elevator newElevator = new Elevator(elevatorRequest.getElevatorId(),
                elevatorRequest.getType(), elevatorRequest.getBuilding() - 'A',
                elevatorRequest.getFloor(), elevatorRequest.getCapacity(),
                elevatorRequest.getSpeed(), elevatorRequest.getSwitchInfo(), requestHandler, this,
                direction);
        if (elevatorRequest.getType().equals("floor")) {
            floorElevators.get(elevatorRequest.getFloor() - 1).add(newElevator);
        }
        elevators.add(newElevator);
        Thread newThread = new Thread(newElevator);
        threads.add(newThread);
        newThread.start();
    }

    public HashSet<Thread> getAllThreads() {
        return threads;
    }
}
