import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputHandler implements Runnable {
    @Override
    public void run() {
        Scheduler scheduler = new Scheduler();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                scheduler.setEnd();
                break;
            } else {
                MyPersonRequest personRequest = new MyPersonRequest(request);
                scheduler.dealPersonRequest(personRequest);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Thread thread : scheduler.getAllThreads()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
