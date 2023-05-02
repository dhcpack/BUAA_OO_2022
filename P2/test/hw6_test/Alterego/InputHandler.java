import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.ArrayList;

public class InputHandler extends Thread {
    //private HashMap<Integer, PersonRequest> personPool;
    private RequestPool personPool;

    public InputHandler(RequestPool personPool) {
        this.personPool = personPool;
    }

    @Override
    public void run() {
        ArrayList<Elevator> elevators = new ArrayList<>();
        ArrayList<CircElevator> circElevators = new ArrayList<>();
        //ElevatorInput elevatorInput =
        //        new ElevatorInput(new TimeInput(System.in).getTimedInputStream());
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            //SafeOutput.println(request.toString());
            if (request == null) {
                synchronized (personPool) {
                    personPool.setEndSign(true);
                    personPool.notifyAll();
                }
                break;
            } else {
                if (request instanceof PersonRequest) {
                    synchronized (personPool) {
                        personPool.addPersonReq((PersonRequest) request);
                        personPool.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    if (((ElevatorRequest) request).getType().equals("building")) {
                        elevators.add(new Elevator(personPool,
                                ((ElevatorRequest) request).getBuilding(),
                                ((ElevatorRequest) request).getElevatorId()));
                        elevators.get(elevators.size() - 1).start();
                    } else if (((ElevatorRequest) request).getType().equals("floor")) {
                        circElevators.add(new CircElevator(personPool,
                                ((ElevatorRequest) request).getFloor(),
                                ((ElevatorRequest) request).getElevatorId()));
                        circElevators.get(circElevators.size() - 1).start();
                    }
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
