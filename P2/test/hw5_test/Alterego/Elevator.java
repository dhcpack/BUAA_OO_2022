import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private int id;
    private int floor;
    private final char buildingString;
    private PassengerList passengerList;
    private ElevatorController elevatorController;
    private PassengerList requestArrayList;
    private int maxSize;
    private TimeOutput timableOutput;

    private void doorOpen() {
        timableOutput.printOutput("OPEN-" + buildingString + "-" +  floor + "-" + id);
        sleepFunction(200);
    }

    private void doorClose() {
        sleepFunction(200);
        timableOutput.printOutput("CLOSE-" + buildingString + "-" +  floor + "-" + id);
    }

    private void personOut(PersonRequest personRequest) {
        timableOutput.printOutput("OUT-" + personRequest.getPersonId() + "-" +
                buildingString + "-" + floor + "-" + id);
    }

    private void personIn(PersonRequest personRequest) {
        timableOutput.printOutput("IN-" + personRequest.getPersonId() + "-" +
                buildingString + "-" + floor + "-" + id);
    }

    private void elevatorMove(int changeFloor) {
        if (changeFloor == floor) {
            return;
        }
        sleepFunction(400);
        if (changeFloor > floor) {
            floor = floor + 1;
        }
        else {
            floor = floor - 1;
        }
        TimableOutput.println("ARRIVE-" + buildingString + "-" + floor + "-" + id);
    }

    private void sleepFunction(long time) {
        synchronized (requestArrayList) {
            try {
                sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Elevator(PassengerList requestArrayList, int id, int maxSize) {
        this.elevatorController = new ElevatorController();
        this.passengerList = new PassengerList();
        this.requestArrayList = requestArrayList;
        this.id = id;
        this.buildingString = (char)(64 + id);
        this.maxSize = maxSize;
        this.floor = 1;
        this.timableOutput = new TimeOutput();
    }

    @Override
    public void run() {
        while (true) {
            boolean doorOpenState = false;
            ArrayList<PersonRequest> nowPassengerList = passengerList.getPassengerLists();
            ArrayList<PersonRequest> temp = new ArrayList<>();
            for (PersonRequest person : nowPassengerList) {
                if (person.getToFloor() == floor) {
                    if (doorOpenState == false) {
                        doorOpen();
                        doorOpenState = true;
                    }
                    //                    synchronized (requestArrayList) {
                    //                        requestArrayList.remove(person);
                    //                    }
                    temp.add(person);
                    personOut(person);
                }
            }
            nowPassengerList.removeAll(temp);
            ElevatorOrder elevatorOrder = elevatorController.calcNext(passengerList,
                    requestArrayList, floor, doorOpenState);
            if (elevatorOrder.getType() == 3) {
                return;
            }

            for (PersonRequest person: elevatorOrder.getPersonRequests()) {  //上电梯的
                if (doorOpenState == false) {
                    doorOpen();
                    doorOpenState = true;
                }
                if (passengerList.size() < maxSize) {
                    passengerList.addPassenger(person);
                    personIn(person);
                    synchronized (requestArrayList) {
                        requestArrayList.remove(person);
                    }
                }
                else {
                    break;
                }
            }
            if (doorOpenState == true) {
                doorClose();
            }
            elevatorMove(elevatorOrder.getDestination());
        }
    }
}
