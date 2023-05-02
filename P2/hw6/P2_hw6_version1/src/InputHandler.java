import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class InputHandler implements Runnable {
    @Override
    public void run() {
        Scheduler scheduler = new Scheduler();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                scheduler.setEnd();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    MyPersonRequest personRequest = new MyPersonRequest((PersonRequest) request);
                    scheduler.dealPersonRequest(personRequest);
                } else if (request instanceof ElevatorRequest) {
                    scheduler.dealElevatorRequest((ElevatorRequest) request);
                }
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
