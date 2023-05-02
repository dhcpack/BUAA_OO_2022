import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private MyQueue<PersonRequest> inputQueue;

    public InputThread(MyQueue<PersonRequest> inputQueue) {
        this.setName("InputThread");
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest personRequest = elevatorInput.nextPersonRequest();
            if (personRequest == null) {
                break;
            } else {
                inputQueue.add(personRequest);
            }
        }

        inputQueue.setEnd(true);

        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(this.getName() + " thread end");
    }
}
