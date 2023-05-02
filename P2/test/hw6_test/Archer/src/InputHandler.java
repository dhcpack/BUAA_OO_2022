import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class InputHandler extends Thread {
    private final ElevatorInput input;
    private final Scheduler scheduler;

    public InputHandler(ElevatorInput input, Scheduler scheduler) {
        this.input = input;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            Request request = input.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                scheduler.setFinished();
                break;
            } else {
                // a new valid request
                if (request instanceof PersonRequest) {
                    // a PersonRequest
                    // your code here
                    scheduler.dispatchRequest(new MyRequest((PersonRequest) request));
                } else if (request instanceof ElevatorRequest) {
                    // an ElevatorRequest
                    // your code here
                    scheduler.executeAddRequest(new MyElevatorRequest((ElevatorRequest) request));
                }
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
