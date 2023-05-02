import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;

public class SubBuilding {
    private char name;
    private final int maxFloor = 10;
    private boolean isEnd;
    private int numOfWaitingPeople;
    private Elevator elevator;
    private HashMap<Integer, MyQueue<PersonRequest>> floorHashMap; // the key is fromFloor

    public SubBuilding(char name) {
        this.name = name;
        isEnd = false;
        floorHashMap = new HashMap<>();
        numOfWaitingPeople = 0;
        for (int i = 1; i <= maxFloor; i++) {
            MyQueue<PersonRequest> people = new MyQueue<PersonRequest>();
            floorHashMap.put(i, people);
        }

        elevator = new Elevator(name, this);
        elevator.start();
    }

    public synchronized void addPerson(PersonRequest person) {
        // System.out.println(name + " add " + person);
        int floor = person.getFromFloor();
        floorHashMap.get(floor).add(person);
        numOfWaitingPeople++;
        notifyAll();
    }

    public synchronized PersonRequest getOnePerson(int floor) {
        PersonRequest person;
        if (floorHashMap.get(floor).isEmpty()) {
            person = null;
        } else {
            person = floorHashMap.get(floor).getOne();
            numOfWaitingPeople--;
        }
        notifyAll();
        return person;
    }

    public synchronized boolean isEmpty() {
        return numOfWaitingPeople == 0;
    }

    public synchronized boolean emptyToWait() {
        boolean ret = false;
        if (numOfWaitingPeople == 0) {
            try {
                // System.out.println(elevator.getName() + " wait");
                ret = true;
                wait();
                // System.out.println(elevator.getName() + " end wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        return ret;
    }

    public synchronized boolean upIsEmpty(int currentFloor) {
        boolean flag = true;
        for (int i = currentFloor + 1; i <= maxFloor; i++) {
            if (!floorHashMap.get(i).isEmpty()) {
                flag = false;
                break;
            }
        }
        notifyAll();
        return flag;
    }

    public synchronized boolean downIsEmpty(int currentFloor) {
        boolean flag = true;
        for (int i = 1; i < currentFloor; i++) {
            if (!floorHashMap.get(i).isEmpty()) {
                flag = false;
                break;
            }
        }
        notifyAll();
        return flag;
    }

    public synchronized boolean currentIsEmpty(int currentFloor) {
        return floorHashMap.get(currentFloor).isEmpty();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return this.isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

}
