import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class CircElevator extends Thread {
    private RequestPool requestPool;
    private HashMap<Character, ArrayList<PersonRequest>> passengers;
    private int floor;
    private int dir; // -1 0 1
    private int id;
    private Predicate<Integer> predBuilding;
    private int capacity;
    private char building;

    public CircElevator(RequestPool requestPool, int floor, int id) {
        this.requestPool = requestPool;
        this.passengers = new HashMap<>();
        this.floor = floor;
        this.id = id;
        this.capacity = 6;
        this.building = 'A';
        this.dir = 1;
        this.predBuilding = new Predicate<Integer>() {
            @Override
            public boolean test(Integer character) {
                return character >= 'A' && character <= 'E';
            }
        };
    }

    @Override
    public void run() {
        while (!(requestPool.isEndSign() && passengers.isEmpty())) {
            while (passengers.isEmpty() && !requestPool.hasReqAhead(
                    this.building, this.floor, 0, this.predBuilding, false)) {
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
                dir = (requestPool.hasReqAhead(building, floor, 1, predBuilding, false)) ? 1 : -1;
            }
            checkDoor();
            if (!passengers.isEmpty()) {
                move();
            } else if (requestPool.hasReqAhead(building, floor, dir, predBuilding, false)) {
                move();
            } else {
                dir = -dir;
            }
        }
    }

    private void move() {
        try {
            sleep((long)(200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int temp = 'A' + (building - 'A' + 5 + dir) % 5;
        building = (char)temp;
        SafeOutput.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    private void checkDoor() {
        //ArrayList<PersonRequest> outPassengers = passengers.get(floor);
        boolean hasOutPassengers = passengers.containsKey(building);
        ArrayList<PersonRequest> outPassengers = passengers.get(building);
        passengers.remove(building);
        boolean hasReqDir = requestPool.hasPersonReq(
                building, floor, dir, predBuilding, false, passengers.isEmpty());
        if (passengers.isEmpty() && !hasReqDir) {
            hasReqDir =
                    requestPool.hasPersonReq(building, floor, dir, predBuilding, false, false);
            dir = (hasReqDir) ? -dir : dir;
        }
        if (hasOutPassengers || hasReqDir) {
            SafeOutput.println("OPEN-" + building + "-" + floor + "-" + id);
            if (hasOutPassengers && !outPassengers.isEmpty()) {
                outPassengers.forEach((u) -> {
                    capacity++;
                    SafeOutput.println("OUT-" + u.getPersonId() + "-" + building + "-" + floor + "-" + id);
                });
            }
            boolean needChkDir = passengers.isEmpty();
            if (capacity > 0 && requestPool.hasPersonReq(
                    building, floor, dir, predBuilding, false, needChkDir)) {
                requestPool.getPersonReq(building, floor, dir, capacity, predBuilding, false, needChkDir).forEach((u) -> {
                    ArrayList<PersonRequest> personRequests;
                    if (passengers.containsKey(u.getToBuilding())) {
                        personRequests = passengers.get(u.getToBuilding());
                    } else {
                        personRequests = new ArrayList<>();
                    }
                    personRequests.add(u);
                    capacity--;
                    passengers.put(u.getToBuilding(), personRequests);
                    SafeOutput.println("IN-" + u.getPersonId() + "-" +
                            building + "-" + floor + "-" + id);
                });
            }
            try {
                sleep((long)(400));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            needChkDir = passengers.isEmpty();
            if (capacity > 0 && requestPool.hasPersonReq(
                    building, floor, dir, predBuilding, false, needChkDir)) {
                requestPool.getPersonReq(building, floor, dir, capacity, predBuilding, false, needChkDir).forEach((u) -> {
                    ArrayList<PersonRequest> personRequests;
                    if (passengers.containsKey(u.getToBuilding())) {
                        personRequests = passengers.get(u.getToBuilding());
                    } else {
                        personRequests = new ArrayList<>();
                    }
                    personRequests.add(u);
                    capacity--;
                    passengers.put(u.getToBuilding(), personRequests);
                    SafeOutput.println("IN-" + u.getPersonId() + "-" + building + "-" + floor + "-" + id);
                });
            }
            SafeOutput.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }
}
