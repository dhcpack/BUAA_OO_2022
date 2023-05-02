import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        PassengerList waitQueue = new PassengerList();

        ArrayList<PassengerList> processingQueues = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            PassengerList parallelQueue = new PassengerList();
            processingQueues.add(parallelQueue);
            Elevator elevator = new Elevator(parallelQueue, i, 6);
            elevator.start();
        }

        Schedule schedule = new Schedule(waitQueue, processingQueues);
        schedule.start();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();

    }
}
