import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {

        TimableOutput.initStartTimestamp();

        ArrayList<RequestQueue> buildingQueues = new ArrayList<>();

        OutputQueue outputQueue = new OutputQueue();

        for (int i = 0; i < 5; i++) {
            RequestQueue parallelQueue = new RequestQueue("building", (char)('A' + i), 1);
            buildingQueues.add(parallelQueue);
            Elevator elevator = new Elevator(parallelQueue,
                    outputQueue, (char)('A' + i), i + 1, 8, 600);
            elevator.start();
        }

        for (int i = 1; i <= 10; i++) {
            RequestQueue parallelQueue = new RequestQueue("floor", 'A', i);
            buildingQueues.add(parallelQueue);
        }

        FloorElevator floorElevator = new FloorElevator(buildingQueues.get(5), outputQueue,
                6, 1, 8, 600, 31);
        floorElevator.start();
        Controller controller = new Controller("controller", buildingQueues);
        controller.start();

        InputThread inputThread = new InputThread(buildingQueues, outputQueue);
        inputThread.start();
    }
}
