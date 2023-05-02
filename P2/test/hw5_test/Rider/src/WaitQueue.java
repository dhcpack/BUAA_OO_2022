import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class WaitQueue {
    private final HashMap<Character,ArrayList<Passenger>> passengers = new HashMap<>();
    private boolean isEnd = false;

    public WaitQueue() {
        passengers.put('A', new ArrayList<>());
        passengers.put('B', new ArrayList<>());
        passengers.put('C', new ArrayList<>());
        passengers.put('D', new ArrayList<>());
        passengers.put('E', new ArrayList<>());
    }

    public synchronized void addPassenger(PersonRequest personRequest) {
        Passenger passenger = new Passenger(personRequest);
        char from = passenger.getFromBuilding();
        passengers.get(from).add(passenger);
        this.notifyAll();
    }

    public synchronized ArrayList<Passenger> getPassenger(char building) {
        ArrayList<Passenger> ans = passengers.get(building);
        passengers.put(building, new ArrayList<>());
        return ans;
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized void setEnd() {
        this.isEnd = true;
        this.notifyAll();
    }

    public synchronized boolean isNull() {
        int sum = 0;
        for (char i = 'A'; i <= 'E'; i++) {
            sum += passengers.get(i).size();
        }
        return sum == 0;
    }

    public synchronized boolean needEnd() {
        if (isEnd && isNull()) {
            return true;
        }
        return false;
    }

    public synchronized void needWait() {
        if (isNull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
