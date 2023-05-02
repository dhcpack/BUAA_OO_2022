import java.util.ArrayList;
import java.util.HashMap;
import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ElevatorRequest;

public class MainClass {
    public static void main(String[] args) throws Exception {
        // for (char c : Util.buildingIncl('E', 'A'))
        // System.out.print(c);
        // 0. init timestamp
        TimableOutput.initStartTimestamp();
        // 1. create queues
        HashMap<Character, MyQueue<PersonRequest>> vertQueues = new HashMap<>();
        HashMap<Integer, MyQueue<PersonRequest>> horiQueues = new HashMap<>();
        for (char building : "ABCDE".toCharArray()) {
            vertQueues.put(building, new MyQueue<>());
        }
        for (int floor = 1; floor <= 10; floor++) {
            horiQueues.put(floor, new MyQueue<>());
        }
        // 2. create threads
        ArrayList<Thread> elevs = new ArrayList<>();
        for (int id = 0; id < 5; id++) {
            char building = (char) ('A' + id);
            ElevVertical elev = new ElevVertical(1 + id, building, vertQueues.get(building));
            elevs.add(elev);
            elev.start();
        }
        // 3. input loop
        Request req;
        ElevatorInput eIn = new ElevatorInput(System.in);
        while ((req = eIn.nextRequest()) != null) {
            if (req instanceof PersonRequest) {
                PersonRequest pReq = (PersonRequest) req;
                if (pReq.getFromFloor() == pReq.getToFloor()) {
                    // same floor -> horizontal request
                    horiQueues.get(pReq.getFromFloor()).put(pReq);
                } else {
                    // vertical request
                    vertQueues.get(pReq.getFromBuilding()).put(pReq);
                }
            }
            if (req instanceof ElevatorRequest) {
                ElevatorRequest eReq = (ElevatorRequest) req;
                if (eReq.getType().equals("floor")) {
                    // horizontal elev
                    ElevHorizontal elev = new ElevHorizontal(eReq.getElevatorId(), eReq.getFloor(),
                            eReq.getBuilding(), horiQueues.get(eReq.getFloor()));
                    elevs.add(elev);
                    elev.start();
                } else {
                    // vertical elev
                    ElevVertical elev = new ElevVertical(eReq.getElevatorId(), eReq.getBuilding(),
                            vertQueues.get(eReq.getBuilding()));
                    elevs.add(elev);
                    elev.start();
                }
            }
        }
        eIn.close();
        // 4. end queues
        for (MyQueue<PersonRequest> q : vertQueues.values()) {
            q.end();
        }
        for (MyQueue<PersonRequest> q : horiQueues.values()) {
            q.end();
        }
        // 5. join threads
        for (Thread t : elevs) {
            t.join();
        }
    }
}
