import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorInput;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        HashMap<ElevatorSlot,ArrayList<RequestQueue>> requestQueues = new HashMap<>();
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RequestQueue requestQueue = new RequestQueue();
            Elevator t = new LookElevator(
                    requestQueue,
                    Elevator.MAX_CARRY,
                    Elevator.Building.values()[i],
                    i + 1
            );
            ElevatorSlot slot = new ElevatorSlot(MyElevatorRequest.ElevatorType.Vertical,i);
            requestQueues.put(slot,new ArrayList<>());
            requestQueues.get(slot).add(requestQueue);
            elevators.add(t);
            t.start();
        }

        Scheduler scheduler = new Scheduler(requestQueues, elevators);
        final InputHandler inputHandler = new InputHandler(elevatorInput, scheduler);

        TimableOutput.initStartTimestamp();
        inputHandler.start();

        inputHandler.join();
        scheduler.joinAll();
    }
}
