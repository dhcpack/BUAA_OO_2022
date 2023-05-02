import com.oocourse.elevator2.PersonRequest;
import java.util.ArrayList;
import java.util.Iterator;

public class Ring extends Thread {
    private final int id;
    private char build;
    private final int floor;
    private int direction;
    private int capacity;
    private boolean door;
    private final RequestQueue processQueue;
    private final ArrayList<PersonRequest> passengers;
    private final Output output;

    public Ring(int id, int floor, RequestQueue processQueue) {
        this.id = id;
        this.build = 'A';
        this.floor = floor;
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
            //directionUpdate();
            turn2();
            personIn();
            closeDoor();
            move();
        }
    }

    private void personOut() {
        Iterator<PersonRequest> iter = passengers.iterator();
        while (iter.hasNext()) {
            PersonRequest request = iter.next();
            if (request.getToBuilding() == build) {
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
                if (request.getFromBuilding() == build) {
                    int diff = (request.getToBuilding() - 'A') - (request.getFromBuilding() - 'A');
                    diff = (diff + 5) % 5;
                    int tmp = (diff <= 2) ? 1 : -1;
                    if (tmp * direction >= 0) {
                        direction = (direction == 0) ? tmp : direction;
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
        int clock = 0;
        int aclock = 0;
        synchronized (processQueue) {
            ArrayList<PersonRequest> waitlist = processQueue.getRequests();
            if (capacity == 0) {
                for (PersonRequest request : waitlist) {
                    int diff = (request.getToBuilding() - 'A') - (request.getFromBuilding() - 'A');
                    diff = (diff + 5) % 5;
                    clock = (diff <= 2) ? clock + 1 : clock;
                    aclock = (diff > 2) ? aclock + 1 : aclock;
                }
                direction = (direction == 1 && clock == 0) ? 0 : direction;
                direction = (direction == -1 && aclock == 0) ? 0 : direction;
                if (direction == 0 && clock + aclock > 0) {
                    direction = (clock >= aclock) ? 1 : -1;
                }
            }
        }
    }

    private int requestNum(char buildIndex, boolean directLock) {
        synchronized (processQueue) {
            ArrayList<PersonRequest> waitlist = processQueue.getRequests();
            int num = 0;
            for (PersonRequest request : waitlist) {
                if (request.getFromBuilding() == buildIndex) {
                    if (directLock) {
                        int d = (request.getToBuilding() - request.getFromBuilding()) * direction;
                        num = (d >= 0) ? num + 1 : num;
                    } else {
                        num++;
                    }
                }
            }
            return num;
        }
    }

    private char getNeighbor(char now, int relative) {
        return (char) ((now - 'A' + relative + 5) % 5 + 'A');
    }

    private void turn() {
        synchronized (processQueue) {
            if (capacity == 0) {
                int num = requestNum(getNeighbor(build,direction), true);
                num += requestNum(getNeighbor(build,2 * direction),true);
                if (num == 0) {
                    int[] relative = {0,1,-1,2,-2};
                    direction = 0;
                    for (int i = 0; i < 5; i++) {
                        if (requestNum(getNeighbor(build,relative[i]),false) > 0) {
                            direction = (relative[i] == 0) ? 0 : ((relative[i] > 0) ? 1 : -1);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void turn2() {
        synchronized (processQueue) {
            if (capacity == 0) {
                boolean next = false;
                int minDis = 5;
                int temp = 0;
                for (PersonRequest request : processQueue.getRequests()) {
                    int rela = request.getFromBuilding() - build;
                    rela = (rela > 2) ? (rela - 5) : rela;
                    rela = (rela < -2) ? (rela + 5) : rela;
                    int d = (request.getToBuilding() - request.getFromBuilding()) * direction;
                    if ((rela == direction || rela == 2 * direction) && d >= 0) {
                        next = true;
                        break;
                    }
                    if (Math.abs(rela) < minDis) {
                        minDis = Math.abs(rela);
                        temp = rela;
                    }
                }
                if (!next) {
                    direction = (temp == 0) ? 0 : (temp > 0) ? 1 : -1;
                }
            }
        }
    }

    private void move() {
        if (direction == 0) {
            processQueue.waitRequest();
        } else {
            int temp = (build - 'A' + direction + 5) % 5;
            build = (char) (temp + 'A');
            asleep(200);
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
