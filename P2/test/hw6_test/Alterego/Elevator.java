import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class Elevator extends Thread {
    private RequestPool requestPool;
    private HashMap<Integer, ArrayList<PersonRequest>> passengers;
    private int floor;
    private int dir; // -1 0 1
    private int id;
    private Predicate<Integer> predFloor;
    private int capacity;
    private char building;

    public Elevator(RequestPool requestPool, char building, int id) {
        this.requestPool = requestPool;
        this.passengers = new HashMap<>();
        this.floor = 1;
        this.id = id;
        this.capacity = 6;
        this.building = building;
        this.dir = 1;
        this.predFloor = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 1 && integer <= 10;
            }
        };
    }

    @Override
    public void run() {
        while (!(requestPool.isEndSign() && passengers.isEmpty())) {
            while (passengers.isEmpty() &&
                    !requestPool.hasReqAhead(this.building, this.floor, 0, this.predFloor, true)) {
                synchronized (this) {
                    if (requestPool.isEndSign() && passengers.isEmpty()) {
                        return;
                    }
                    try {
                        this.wait(20L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (dir == 0) {
                dir = (requestPool.hasReqAhead(building, floor, 1, predFloor, true)) ? 1 : -1;
            }
            checkDoor();
            if (!passengers.isEmpty()) {
                move();
            } else if (requestPool.hasReqAhead(building, floor, dir, predFloor, true)) {
                move();
            } else {
                dir = -dir;
            }
        }
    }

    private void move() {
        try {
            sleep((long)(400));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor += dir;
        SafeOutput.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    private void checkDoor() {
        //ArrayList<PersonRequest> outPassengers = passengers.get(floor);
        if (passengers.containsKey(floor) ||
                requestPool.hasPersonReq(building, floor, dir, predFloor, true, false)) {
            SafeOutput.println("OPEN-" + building + "-" + floor + "-" + id);
            if (passengers.containsKey(floor) && !passengers.get(floor).isEmpty()) {
                passengers.get(floor).forEach((u) -> {
                    capacity++;
                    SafeOutput.println("OUT-" + u.getPersonId() +
                            "-" + building + "-" + floor + "-" + id);
                });
                passengers.remove(floor);
            }
            if (capacity > 0 && requestPool.hasPersonReq(
                    building, floor, dir, predFloor, true, false)) {
                requestPool.getPersonReq(building, floor, dir, capacity, predFloor, true, false).forEach((u) -> {
                    ArrayList<PersonRequest> personRequests;
                    if (passengers.containsKey(u.getToFloor())) {
                        personRequests = passengers.get(u.getToFloor());
                    } else {
                        personRequests = new ArrayList<>();
                    }
                    personRequests.add(u);
                    capacity--;
                    passengers.put(u.getToFloor(), personRequests);
                    SafeOutput.println("IN-" + u.getPersonId() +
                            "-" + building + "-" + floor + "-" + id);
                });
            }
            try {
                sleep((long)(400));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (capacity > 0 && requestPool.hasPersonReq(
                    building, floor, dir, predFloor, true, false)) {
                requestPool.getPersonReq(building, floor, dir, capacity, predFloor, true, false).forEach((u) -> {
                    ArrayList<PersonRequest> personRequests;
                    if (passengers.containsKey(u.getToFloor())) {
                        personRequests = passengers.get(u.getToFloor());
                    } else {
                        personRequests = new ArrayList<>();
                    }
                    personRequests.add(u);
                    capacity--;
                    passengers.put(u.getToFloor(), personRequests);
                    SafeOutput.println("IN-" + u.getPersonId() +
                            "-" + building + "-" + floor + "-" + id);
                });
            }
            SafeOutput.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }
}
