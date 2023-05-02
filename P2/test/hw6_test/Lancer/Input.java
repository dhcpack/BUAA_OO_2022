import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.Objects;

public class Input extends Thread {
    private static final Input INSTANCE = new Input();

    private Input() {
    }

    public static Input getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                ElevatorSystem.getInstance().notifyEle();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest) request;
                    Elevator target = ElevatorSystem.getInstance().dispatch(personRequest);
                    synchronized (target.getQueue()) {
                        target.getQueue().addWait(personRequest);
                        if (!target.getQueue().isSleep()) {
                            target.getQueue().notifyAll();
                        }
                    }
                } else {
                    //char building = ((ElevatorRequest) request).getBuilding();
                    //ElevatorQueue y = ElevatorSystem.getInstance().getElevators(building);
                    ElevatorRequest request1 = (ElevatorRequest) request;
                    if (Objects.equals(request1.getType(), "building")) {
                        int num = request1.getElevatorId();
                        char building = request1.getBuilding();
                        VerElevator newOne = Default.defaultVerElevator(num, building);
                        ElevatorSystem.getInstance().addVertical(newOne, building);
                    } else {
                        int num = request1.getElevatorId();
                        int floor = request1.getFloor();
                        HoriElevator newOne = Default.defaultHoriElevator(num, floor);
                        ElevatorSystem.getInstance().addHorizontal(newOne, floor);
                    }
                }
            }
        }
    }
}
