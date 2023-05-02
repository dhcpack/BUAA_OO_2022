import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;
    private final ArrayList<RequestQueue> buildingQueues;
    private final OutputQueue outputQueue;
    private int requestNum = 0;

    public InputThread(ArrayList<RequestQueue> buildingQueues, OutputQueue outputQueue) {
        this.waitQueue = WaitQueue.getInstance();
        this.buildingQueues = buildingQueues;
        this.outputQueue = outputQueue;
    }

    private int getType(double in) {
        int res = (int) (in * 10) / 2 - 1;
        if (res > 2 || res < 0) {
            System.out.println("invalid speed!");
            res = 0;
        }
        return res;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (int i = 0; i < requestNum; ++i) { //查收所有的任务
                    RequestCounter.getInstance().acquire();
                }
                waitQueue.setEnd(true);
                // System.out.println("Input End");
                return;
            } else {
                if (request instanceof PersonRequest) {
                    requestNum++;
                    PersonRequest personRequest = (PersonRequest) request;
                    Person person = new Person(personRequest.getPersonId(),
                            personRequest.getFromBuilding(), personRequest.getFromFloor(),
                            personRequest.getToBuilding(), personRequest.getToFloor());
                    person = TransferMap.getInstance().split(person);
                    waitQueue.addRequest(person);// 在输入线程中拆分，避免对请求的重复拆分
                } else if (request instanceof ElevatorRequest) {
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    if (elevatorRequest.getType().equals("building")) {
                        /*
                        TransferMap.getInstance().addCnt(true, elevatorRequest.getBuilding(),
                                1, getType(elevatorRequest.getSpeed()));

                         */
                        // 0-4
                        Elevator elevator = new Elevator(buildingQueues.get(
                                elevatorRequest.getBuilding() - 'A'), outputQueue,
                                elevatorRequest.getBuilding(), elevatorRequest.getElevatorId(),
                                elevatorRequest.getCapacity(),
                                (int)(elevatorRequest.getSpeed() * 1000));
                        elevator.start();
                    } else {
                        /*
                        TransferMap.getInstance().addCnt(false, 'A',
                                elevatorRequest.getFloor(), getType(elevatorRequest.getSpeed()));

                         */
                        // 5-14
                        TransferMap.getInstance().add(elevatorRequest.getFloor(),
                                elevatorRequest.getSwitchInfo());
                        FloorElevator floorElevator = new FloorElevator(buildingQueues.get(
                                elevatorRequest.getFloor() + 4), outputQueue,
                                elevatorRequest.getElevatorId(), elevatorRequest.getFloor(),
                                elevatorRequest.getCapacity(),
                                (int)(elevatorRequest.getSpeed() * 1000),
                                elevatorRequest.getSwitchInfo());
                        floorElevator.start();
                    }
                }
            }
        }
    }
}
