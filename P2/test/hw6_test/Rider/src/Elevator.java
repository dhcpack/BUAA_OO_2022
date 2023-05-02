import com.oocourse.elevator2.PersonRequest;
import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private final int id;
    private char build;
    private int floor;
    private int direction;
    private int capacity;
    private boolean door;
    private final RequestQueue processQueue;
    private final ArrayList<PersonRequest> passengers;
    private final Output output;

    public Elevator(int id, char build, RequestQueue processQueue) {
        this.id = id;
        this.build = build;
        this.floor = 1;
        this.direction = 0;
        this.capacity = 0;
        this.door = false;
        this.processQueue = processQueue;
        passengers = new ArrayList<>();
        this.output = Output.getInstance();
    }

    public void asleep(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            if (processQueue.isEnd() && processQueue.isEmpty() && direction == 0) {
                return;
            }
            personOut();
            personIn();
            directionUpdate();
            personIn();
            closeDoor();
            move();
        }
    }

    private void personOut() {
        Iterator<PersonRequest> iter = passengers.iterator();
        while (iter.hasNext()) {
            PersonRequest request = iter.next();
            if (request.getToFloor() == floor) {
                openDoor();
                output.print(String.format("OUT-%d-%c-%d-%d",request.getPersonId(),build,floor,id));
                iter.remove();
                capacity--;
            }
        }
    }

    private void personIn() {
        synchronized (processQueue) {
            ArrayList<PersonRequest> waitlist = processQueue.getRequests();
            Iterator<PersonRequest> iter = waitlist.iterator();
            while (iter.hasNext() && capacity < 6) {
                PersonRequest request = iter.next();
                if (request.getFromFloor() == floor) {
                    if ((request.getToFloor() - request.getFromFloor()) * direction >= 0) {
                        if (direction == 0) {
                            direction = (request.getToFloor() > request.getFromFloor()) ? 1 : -1;
                        }
                        openDoor();
                        passengers.add(request);
                        output.print(String.format(
                                "IN-%d-%c-%d-%d",request.getPersonId(),build,floor,id));
                        iter.remove();
                        capacity++;
                    }
                }
            }
        }
    }

    private void directionUpdate() {
        int low = 0;
        int high = 0;
        synchronized (processQueue) {
            ArrayList<PersonRequest> waitlist = processQueue.getRequests();
            if (capacity == 0) {
                for (PersonRequest request : waitlist) {
                    low = (request.getFromFloor() < floor) ? low + 1 : low;
                    high = (request.getFromFloor() > floor) ? high + 1 : high;
                }
                direction = (direction == 1 && high == 0) ? 0 : direction;
                direction = (direction == -1 && low == 0) ? 0 : direction;
                if (direction == 0 && high + low > 0) {
                    direction = (high >= low) ? 1 : -1;
                }
            }
        }
    }

    private void move() {
        if (direction == 0) {
            processQueue.waitRequest();
        } else {
            floor += direction;
            asleep(400);
            output.print(String.format("ARRIVE-%c-%d-%d",build,floor,id));
        }
    }

    private void openDoor() {
        if (!door) {
            door = true;
            output.print(String.format("OPEN-%c-%d-%d",build,floor,id));
            asleep(400);
        }
    }

    private void closeDoor() {
        if (door) {
            door = false;
            output.print(String.format("CLOSE-%c-%d-%d",build,floor,id));
        }
    }
}
