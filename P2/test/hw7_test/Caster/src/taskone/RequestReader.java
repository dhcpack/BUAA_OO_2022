package taskone;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import tasktwo.Controller;

public class RequestReader implements Runnable {
    private ElevatorInput elevatorInput;

    private Controller controller;

    public RequestReader(ElevatorInput elevatorInput, Controller controller) {
        this.elevatorInput = elevatorInput;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = elevatorInput.nextRequest();
                if (request == null) {
                    controller.add(new PersonRequest(0, 0, '0', '0', 1));
                    break;
                }
                controller.add(request);
            }
            elevatorInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
