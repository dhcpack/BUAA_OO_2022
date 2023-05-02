import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private WaitQueue waitQueue;

    public Input(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                waitQueue.setEnd();
                //System.out.println("input end");
                break;
            } else {
                waitQueue.addPassenger(request);
                //System.out.println("addPerson" + request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
