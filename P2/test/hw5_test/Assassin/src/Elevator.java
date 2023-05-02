import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private PeopleQueue processingQueue;
    private ArrayList<People> passengers;
    private char building;
    private int floor;
    private int direction;
    private int toFloor;
    private int id;

    public Elevator(PeopleQueue processingQueue, char building) {
        this.processingQueue = processingQueue;
        this.building = building;
        this.passengers = new ArrayList<>();
        this.floor = 1;
        this.direction = 0;
        this.toFloor = 1;
        this.id = building - 'A' + 1;
    }

    @Override
    public void run() {
        int need;
        while (true) {
            if (passengers.isEmpty() && processingQueue.isEmpty() && processingQueue.isEnd()) {
                return;
            }
            if (direction == 0) {
                toFloor = processingQueue.nextFloor();
                if (toFloor == -1) {
                    continue;
                }
                direction = (toFloor > floor) ? 1 :
                        (toFloor < floor) ? -1 : 0;
            }
            move();
            need = needOpen();
            if (need == 1) {
                open();
                outPassenger();
                getPassenger();
                close();
            } else if (need == 2) {
                open();
                getPassenger();
                close();
            }
        }
    }

    public void move() {
        try {
            if (direction != 0) {
                sleep(400);
                floor += direction;
                TimableOutput.println(String.format("ARRIVE-%c-%d-%d", building, floor, id));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getPassenger() {
        while (passengers.size() < 6) {
            if (floor == toFloor) {
                if (passengers.isEmpty()) {
                    direction = 0;
                } else {
                    toFloor = passengers.get(0).getToFloor();
                    direction = (toFloor > floor) ? 1 :
                            (toFloor < floor) ? -1 : 0;
                }
            }
            People passenger = processingQueue.getOnePassenger(floor, direction);
            if (passenger == null) {
                break;
            }
            passengers.add(passenger);
            TimableOutput.println(String.format("IN-%d-%c-%d-%d", passenger.getId(),
                    building, floor, id));
        }
    }

    public void outPassenger() {
        for (int i = 0; i < passengers.size(); i++) {
            People passenger = passengers.get(i);
            if (passenger.getToFloor() == floor) {
                TimableOutput.println(String.format("OUT-%d-%c-%d-%d", passenger.getId(),
                        building, floor, id));
                passengers.remove(i);
                i--;
            }
        }
        return;
    }

    public int needOpen() {
        for (People passenger : passengers) {
            if (passenger.getToFloor() == floor) {
                return 1;
            }
        }
        return processingQueue.hasPassenger(floor);
    }

    public void open() {
        TimableOutput.println(String.format("OPEN-%c-%d-%d", building, floor, id));
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        TimableOutput.println(String.format("CLOSE-%c-%d-%d", building, floor, id));
    }
}
