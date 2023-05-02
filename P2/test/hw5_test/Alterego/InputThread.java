import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.ElevatorInput;

import java.io.IOException;

public class InputThread extends Thread {
    private PassengerList waitQueue;

    public InputThread(PassengerList waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest personRequest = elevatorInput.nextPersonRequest();
            if (personRequest == null) {
                waitQueue.setEnd(true);
                try {
                    elevatorInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            } else {
                PersonRequest personRequest1 = new PersonRequest(
                        personRequest.getFromFloor(),
                        personRequest.getToFloor(),
                        personRequest.getFromBuilding(),
                        personRequest.getToBuilding(),
                        personRequest.getPersonId()
                );
                waitQueue.addPassenger(personRequest1);
            }
        }
    }
}
