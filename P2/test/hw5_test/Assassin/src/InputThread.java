import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private PeopleQueue waitQueue;

    public InputThread(PeopleQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest person = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (person == null) {
                waitQueue.setEnd(true);
                break;
            } else {
                // a new valid request
                People people = new People(person.getPersonId(), person.getFromFloor(),
                        person.getToFloor(), person.getFromBuilding());
                waitQueue.addPeople(people);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
