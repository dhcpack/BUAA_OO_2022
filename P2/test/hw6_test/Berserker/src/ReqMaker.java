import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

public class ReqMaker extends Thread {
    private final TowerReqQueue[] towerReqQueues;
    private final FloorReqQueue[] floorReqQueues;

    public ReqMaker(TowerReqQueue[] towerReqQueues, FloorReqQueue[] floorReqQueues) {
        super("ReqMaker");
        this.floorReqQueues = floorReqQueues;
        this.towerReqQueues = towerReqQueues;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (TowerReqQueue q : towerReqQueues) {
                    q.end();
                }
                for (FloorReqQueue q : floorReqQueues) {
                    q.end();
                }
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest pr = (PersonRequest) request;
                    MyRequest mr = new MyRequest(pr.getPersonId(), pr.getFromBuilding(),
                            pr.getFromFloor(), pr.getToBuilding(), pr.getToFloor());
                    if (pr.getFromFloor() == pr.getToFloor()) {
                        floorReqQueues[pr.getFromFloor() - 1].putReq(mr);
                    } else if (pr.getFromBuilding() == pr.getToBuilding()) {
                        towerReqQueues[pr.getToBuilding() - 'A'].putReq(mr);
                    } else {
                        System.out.println("Illegal person request!");
                    }
                } else if (request instanceof ElevatorRequest) {
                    ElevatorRequest er = (ElevatorRequest) request;
                    if (er.getType().equals("building")) {
                        FloorElevator elevator = new FloorElevator(er.getElevatorId(),
                                er.getBuilding(), towerReqQueues[er.getBuilding() - 'A']);
                        elevator.start();
                    } else if (er.getType().equals("floor")) {
                        TowerElevator elevator = new TowerElevator(er.getElevatorId(),
                                er.getFloor(), floorReqQueues[er.getFloor() - 1]);
                        elevator.start();
                    } else {
                        System.out.println("Unknown elevator type!");
                    }
                } else {
                    System.out.println("Unknown request type!");
                }
            }
        }
    }
}
