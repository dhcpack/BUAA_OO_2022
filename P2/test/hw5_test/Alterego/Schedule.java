import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Schedule extends Thread {
    private PassengerList waitQueue;
    private ArrayList<PassengerList> processingQueues;

    public Schedule(PassengerList waitQueue
            , ArrayList<PassengerList> processingQueues) {
        this.waitQueue = waitQueue;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() == true && waitQueue.isEnd() == true) {
                for (PassengerList passengerList: processingQueues) {
                    passengerList.setEnd(true);
                }
                return;
            }
            PersonRequest personRequest = waitQueue.getOnePersonRequest();
            if (personRequest == null) {
                continue;
            }
            int building = personRequest.getFromBuilding() - 'A';
            processingQueues.get(building).addPassenger(personRequest);
        }
    }
}
