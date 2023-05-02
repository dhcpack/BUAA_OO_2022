import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        PeopleQueue waitQueue = new PeopleQueue();
        ArrayList<PeopleQueue> processingQueues = new ArrayList<>();
        TimableOutput.initStartTimestamp();
        for (int i = 0; i < 5; i++) {
            PeopleQueue processingQueue = new PeopleQueue();
            processingQueues.add(processingQueue);
            Elevator elevator = new Elevator(processingQueue, (char) ('A' + i));
            elevator.start();
        }

        Schedule schedule = new Schedule(waitQueue, processingQueues);
        schedule.start();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();
    }
}
