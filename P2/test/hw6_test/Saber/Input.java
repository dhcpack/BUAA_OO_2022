import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.ArrayList;
import java.util.Vector;

public class Input extends Thread {
    private final RequestQueue waitQueue;
    private final int[] hasCircleBuilding;
    private ArrayList<RequestQueue> allBuildingList;
    private Vector<RequestQueue> allCircleList;
    private Vector<Thread> allElevator;
    private RequestQueue changeList;

    public Input(RequestQueue waitQueue, ArrayList<RequestQueue> allBuildingList,
                 Vector<RequestQueue> allCircleList, int[] hasCircleBuilding,
                 Vector<Thread> allElevator, RequestQueue changeList) {
        this.waitQueue = waitQueue;
        this.allBuildingList = allBuildingList;
        this.allCircleList = allCircleList;
        this.hasCircleBuilding = hasCircleBuilding;
        this.allElevator = allElevator;
        this.changeList = changeList;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        //ElevatorInput elevatorInput = new ElevatorInput
        // (new TimeInput(System.in).getTimedInputStream());
        while (true) {
            //System.out.printf("thread input %d + running \n",this.getId());
            Request req = elevatorInput.nextRequest();
            if (req == null) {
                waitQueue.setEnd(true);
                return;
            } else {
                if (req instanceof PersonRequest) {
                    PersonRequest p = (PersonRequest) req;
                    MoveRequest pr = new MoveRequest(p.getFromFloor(), p.getToFloor(),
                            p.getFromBuilding(), p.getToBuilding(), p.getPersonId());
                    waitQueue.addRequest(pr);
                } else if (req instanceof ElevatorRequest) {
                    ElevatorRequest e = (ElevatorRequest) req;
                    if (e.getType().equals("building")) {
                        //纵向电梯
                        Elevator newEle = new Elevator(e.getElevatorId(), e.getBuilding(),
                                1, allBuildingList.get(e.getBuilding() - 'A'),
                                allCircleList, changeList);
                        allElevator.add(newEle);
                        newEle.setName("ele " + e.getElevatorId());
                        newEle.start();
                    } else if (e.getType().equals("floor")) {
                        CircleElevator newCele = new CircleElevator(e.getElevatorId()
                                , e.getFloor(), 1,
                                allCircleList.get(e.getFloor() - 1), allBuildingList, changeList);
                        allElevator.add(newCele);
                        newCele.setName("circle " + e.getElevatorId());
                        newCele.start();
                        synchronized (hasCircleBuilding) {
                            hasCircleBuilding[e.getFloor() - 1]++;
                        }
                    }
                }
            }
        }
    }
}

