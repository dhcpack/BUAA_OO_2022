import java.util.ArrayList;
import com.oocourse.elevator1.PersonRequest;

public class PassengerList {
    private ArrayList<PersonRequest> passengerLists;
    private boolean isEnd;

    public void printSelf() {
        System.out.println("The size is");
        System.out.println(passengerLists.size());
        for (PersonRequest personRequest: passengerLists) {
            System.out.println(personRequest.toString());
        }
    }

    public PassengerList() {
        this.passengerLists = new ArrayList<>();
        this.isEnd =  false;
    }

    public synchronized int size() {
        notifyAll();
        return this.passengerLists.size();
    }

    public synchronized void addPassenger(PersonRequest passenger) {
        passengerLists.add(passenger);
        notifyAll();
    }

    public synchronized ArrayList<PersonRequest> getPassengerLists() {
        notifyAll();
        return this.passengerLists;
    }

    public synchronized void remove(PersonRequest toRemove) {
        this.passengerLists.remove(toRemove);
        notifyAll();
        return;
    }

    public synchronized PersonRequest getOnePersonRequest() {
        if (!isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (passengerLists.isEmpty()) {
            return null;
        }
        PersonRequest personRequest = passengerLists.get(0);
        passengerLists.remove(0);
        notifyAll();
        return personRequest;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return this.isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return this.passengerLists.isEmpty();
    }
}
