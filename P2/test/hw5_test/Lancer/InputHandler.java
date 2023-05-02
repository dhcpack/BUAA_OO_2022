import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputHandler extends Thread {
    private RequestQueue buffer;

    public InputHandler(RequestQueue buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();

            if (request == null) {
                buffer.setEnd(true);
                return;
            } else {
                Request newRequest = new Request(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor(),
                        request.getFromBuilding(), request.getToBuilding());
                buffer.addRequest(newRequest);
            }
        }
    }
}
