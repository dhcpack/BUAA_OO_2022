import java.util.ArrayList;

public class WaitQueue {
    private static final WaitQueue WAIT_QUEUE = new WaitQueue();
    private final ArrayList<Person> persons = new ArrayList<>();
    private boolean isEnd;

    WaitQueue() {
        isEnd = false;
    }

    public static WaitQueue getInstance() {
        return WAIT_QUEUE;
    }

    public synchronized void setEnd(boolean inputEnd) {
        this.isEnd = inputEnd;
        // System.out.println("WaitQueue end");
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        return persons.isEmpty();
    }

    public synchronized void addRequest(Person request) {
        persons.add(request);
        notifyAll();
    }

    public synchronized Person getOneRequest() {
        if (!isEnd && persons.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (persons.isEmpty()) {
            return null;
        }
        Person person = persons.get(0);
        persons.remove(0);
        notifyAll();
        return person;
    }
}
