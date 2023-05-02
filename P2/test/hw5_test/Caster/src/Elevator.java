import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;

public class Elevator extends Thread {
    private final int up = 1;
    private final int down = -1;
    private final int capacity = 6;
    private final int maxFloor = 10;

    private final int id;
    private final char buildingBelongs;
    private int currentFloor;
    private int currentHeading;
    private int curNumOfPeople;
    private final SubBuilding subBuilding;
    private HashMap<Integer, MyQueue<PersonRequest>> people;

    public Elevator(char buildingBelongs, SubBuilding subBuilding) {
        this.id = (int)buildingBelongs - 'A' + 1;
        this.buildingBelongs = buildingBelongs;
        this.subBuilding = subBuilding;

        currentFloor = 1;
        currentHeading = up;
        curNumOfPeople = 0;

        people = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            people.put(i, new MyQueue<PersonRequest>());
        }

        this.setName("Elevator " + buildingBelongs + " " + id);
    }

    @Override
    public void run() {
        while (true) {
            // System.out.println(this.getName() + " here1");
            if (toOpen()) {
                openTheDoor();
                personGetOut();
                personGetIn();
                closeTheDoor();
            }

            //System.out.println(this.getName() + "curNumOfPeople:" + curNumOfPeople);

            if (subBuilding.isEnd() && subBuilding.isEmpty() && curNumOfPeople == 0) {
                break;
            }

            if (curNumOfPeople == 0) {
                boolean waitSuccess = subBuilding.emptyToWait(); // judge whether to wait
                if (waitSuccess) {
                    continue;
                }
            }
            // System.out.println(this.getName() + " here3");

            if (!hasRequest(currentHeading)) {
                currentHeading *= -1;
            }

            move(currentHeading);
            // System.out.println(this.getName() + " here2");
        }
        //System.out.println(this.getName() + " thread end");
    }

    private boolean hasRequest(int direction) {
        int step = direction == up ? 1 : -1;
        int floor = currentFloor + step;

        boolean flag = false;
        // whether there is someone to get off
        while (floor > 0 && floor <= maxFloor) {
            if (!people.get(floor).isEmpty()) {
                flag = true;
                break;
            }
            floor += step;
        }

        // whether there is someone to get in
        if (!flag) {
            if (direction == up && !subBuilding.upIsEmpty(currentFloor)) {
                flag = true;
            } else if (direction == down && !subBuilding.downIsEmpty(currentFloor)) {
                flag = true;
            }
        }

        return flag;
    }

    private void personGetOut() {
        MyQueue<PersonRequest> queue = people.get(currentFloor);
        while (!queue.isEmpty()) {
            PersonRequest person = queue.getOne();
            curNumOfPeople--;
            OutputClass.println(String.format("OUT-%d-%s", person.getPersonId(), toString()));
        }
    }

    private void personGetIn() {
        while (curNumOfPeople < capacity) {
            PersonRequest person = subBuilding.getOnePerson(currentFloor);
            if (person == null) {
                break;
            }
            people.get(person.getToFloor()).add(person);
            curNumOfPeople++;
            OutputClass.println(String.format("IN-%d-%s", person.getPersonId(), toString()));
        }
    }

    private void move(int direction) {
        sleepForATime(400);
        if (direction == up) {
            currentFloor++;
        } else {
            currentFloor--;
        }
        OutputClass.println(String.format("ARRIVE-%s", toString()));
    }

    private boolean toOpen() {
        return (!people.get(currentFloor).isEmpty()) || (!subBuilding.currentIsEmpty(currentFloor));
    }

    private void openTheDoor() {
        OutputClass.println(String.format("OPEN-%s", toString()));
        sleepForATime(200);
    }

    private void closeTheDoor() {
        sleepForATime(200);
        OutputClass.println(String.format("CLOSE-%s", toString()));
    }

    private void sleepForATime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%c-%d-%d", buildingBelongs, currentFloor, id);
    }

}
