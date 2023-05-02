import java.util.ArrayList;

public class PeopleQueue {
    private ArrayList<People> peoples;
    private boolean isEnd;

    public PeopleQueue() {
        this.peoples = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addPeople(People people) {
        peoples.add(people);
        notifyAll();
    }

    public synchronized People getOnePeople() {
        if (peoples.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (peoples.isEmpty()) {
            return null;
        }
        People people = peoples.get(0);
        peoples.remove(0);
        notifyAll();
        return people;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return peoples.isEmpty();
    }

    public synchronized int nextFloor() {
        if (peoples.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (peoples.isEmpty()) {
            return -1;
        }
        notifyAll();
        return peoples.get(0).getFromFloor();
    }

    public synchronized People getOnePassenger(int floor, int direction) {
        if (peoples.isEmpty()) {
            notifyAll();
            return null;
        }
        int next = -1;
        for (int i = 0; i < peoples.size(); i++) {
            People people = peoples.get(i);
            if (people.getFromFloor() == floor) {
                if (people.getDirection() == direction || direction == 0) {
                    if (next == -1) {
                        next = i;
                    } else if (people.compareTo(peoples.get(next)) == 1) {
                        next = i;
                    }
                }
            }
        }
        if (next != -1) {
            People people = peoples.get(next);
            peoples.remove(next);
            notifyAll();
            return people;
        }
        notifyAll();
        return null;
    }

    public synchronized int hasPassenger(int floor) {
        for (People people : peoples) {
            if (people.getFromFloor() == floor) {
                return 2;
            }
        }
        return 0;
    }
}
