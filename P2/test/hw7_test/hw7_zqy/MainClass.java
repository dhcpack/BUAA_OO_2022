import java.util.ArrayList;
import com.oocourse.TimableOutput;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;

public class MainClass {
    public static final int FLOORS = 10;

    public static void main(String[] args) throws Exception {
        // 0. init timestamp
        TimableOutput.initStartTimestamp();
        // 1. create queues, start elevators
        MySem sem = new MySem();
        MidFinder midFinder = new MidFinder(FLOORS);
        BuildingQueues queues = new BuildingQueues(sem);
        ArrayList<Thread> elevators = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            Thread elevVertical = new ElevVertical(1 + i, (char) ('A' + i), 0.6, 8, 1, queues);
            elevators.add(elevVertical);
            elevVertical.start();
        }
        {
            SwitchInfo si = new SwitchInfo(0b11111);
            Thread elevHoriz = new ElevHorizontal(6, 1, 0.6, 8, 'A', queues, si);
            midFinder.addSwitchInfo(1, si); // initial horizontal elevator
            elevators.add(elevHoriz);
            elevHoriz.start();
        }
        // 2. read input loop
        Request req;
        ElevatorInput eIn = new ElevatorInput(System.in);
        while ((req = eIn.nextRequest()) != null) {
            if (req instanceof PersonRequest) {
                PersonRequest pReq = (PersonRequest) req;
                MyPerson person = new MyPerson(pReq.getPersonId(), pReq.getFromFloor(),
                        midFinder.findMidFloor(pReq), pReq.getToFloor(), pReq.getFromBuilding(),
                        pReq.getToBuilding());
                queues.putIntoQueues(person);
            } else if (req instanceof ElevatorRequest) {
                ElevatorRequest eReq = (ElevatorRequest) req;
                Thread threadElev;
                if (eReq.getType().equals("floor")) {
                    SwitchInfo si = new SwitchInfo(eReq.getSwitchInfo());
                    midFinder.addSwitchInfo(eReq.getFloor(), si);
                    threadElev = new ElevHorizontal(eReq.getElevatorId(), eReq.getFloor(),
                            eReq.getSpeed(), eReq.getCapacity(), eReq.getBuilding(), queues, si);
                } else {
                    threadElev = new ElevVertical(eReq.getElevatorId(), eReq.getBuilding(),
                            eReq.getSpeed(), eReq.getCapacity(), eReq.getFloor(), queues);
                }
                elevators.add(threadElev);
                threadElev.start();
            }
        }
        eIn.close();
        // 3. set end
        while (sem.getCount() < elevators.size()) {
            sem.waitForChange();
        }
        queues.end();
        // 4. join threads
        for (Thread t : elevators) {
            t.join();
        }
    }
}
