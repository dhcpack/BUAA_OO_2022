import com.oocourse.elevator1.PersonRequest;

public class ElevatorController {
    public ElevatorOrder calcNext(PassengerList passengerList,
                                  PassengerList requestArrayList, int floor, boolean door) {
        synchronized (requestArrayList) {
            ElevatorOrder elevatorOrder = new ElevatorOrder();
            PersonRequest majorPersonRequest = new PersonRequest(0, 0, '0', '0', 0);
            PassengerList returnArrayList = new PassengerList();
            int destination = floor;
            while (true) {
                if (requestArrayList.isEmpty() == false || passengerList.isEmpty() == false) {
                    break;
                }
                else if (door == true) {
                    elevatorOrder.setType(0);
                    elevatorOrder.setDestination(floor);
                    elevatorOrder.setMajorRequest(majorPersonRequest);
                    elevatorOrder.setPersonRequests(requestArrayList.getPassengerLists());
                    return elevatorOrder;
                }
                else if (requestArrayList.isEnd() == true && requestArrayList.isEmpty() == true
                        && passengerList.isEmpty() == true) {
                    elevatorOrder.setType(3);
                    return elevatorOrder;
                }
                else if (requestArrayList.isEmpty() == true && passengerList.isEmpty() == true) {
                    try {
                        requestArrayList.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (passengerList.size() == 0) {        //找主请求 电梯没人
                majorPersonRequest = requestArrayList.getPassengerLists().get(0);
                destination = majorPersonRequest.getFromFloor();
            } else {                      //电梯有人
                majorPersonRequest = passengerList.getPassengerLists().get(0);      //先到的人在队头
                if (majorPersonRequest.getToFloor() == floor) {
                    returnArrayList.addPassenger(majorPersonRequest);
                    elevatorOrder.setType(2);
                    elevatorOrder.setPersonRequests(requestArrayList.getPassengerLists());
                    elevatorOrder.setMajorRequest(majorPersonRequest);
                    elevatorOrder.setDestination(majorPersonRequest.getToFloor());
                    return elevatorOrder;
                }
                destination = majorPersonRequest.getToFloor();
            }
            for (PersonRequest person : requestArrayList.getPassengerLists()) {
                if (person.getFromFloor() == floor && ((person.getToFloor() - floor)
                        * (destination - floor) >= 0)) {  //同方向
                    returnArrayList.addPassenger(person);
                }
            }
            elevatorOrder.setType(0);    //相等的已经下去了
            elevatorOrder.setPersonRequests(returnArrayList.getPassengerLists());
            elevatorOrder.setMajorRequest(majorPersonRequest);
            elevatorOrder.setDestination(destination);
            return elevatorOrder;
        }
    }
}
