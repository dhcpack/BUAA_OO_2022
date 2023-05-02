import com.oocourse.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();
        HashMap<Character, BuildingWaitQueue> buildingWaitQueues = new HashMap<>();
        for (char i = 'A'; i <= 'E'; i++) {
            buildingWaitQueues.put(i, new BuildingWaitQueue(i));
        }
        WaitQueue waitQueue = new WaitQueue();

        Input input = new Input(waitQueue);
        input.start();

        Scheduler scheduler = new Scheduler(waitQueue, buildingWaitQueues);
        scheduler.start();

        for (char i = 'A'; i <= 'E'; i++) {
            Elevator elevator = new Elevator(i, i - 'A' + 1,
                    200, 200,
                    400, buildingWaitQueues.get(i), 6);
            elevator.start();
        }
    }
}
