import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyQueue<T> extends ArrayList<T> {
    private final MySem sem;

    public MyQueue(MySem sem) {
        super();
        this.sem = sem;
    }

    private boolean notEnded = true;

    public synchronized void put(T obj) {
        if (notEnded) {
            add(obj);
        }
        notifyAll();
    }

    public synchronized void end() {
        notEnded = false;
        notifyAll();
    }

    // wait for the queue to be not empty, false when ended
    public synchronized boolean waitForPut() throws InterruptedException {
        if (isEmpty() && notEnded) {
            sem.inc();
            wait();
            sem.dec();
        }
        return notEnded;
    }

    // wait for next put, even not empty
    public synchronized boolean waitForNextPut() throws InterruptedException {
        if (notEnded) {
            sem.inc();
            wait();
            sem.dec();
        }
        return notEnded;
    }

    public synchronized T tryGetFirst() {
        return isEmpty() ? null : get(0);
    }

    public synchronized boolean syncRemoveIf(Predicate<T> filter) {
        return removeIf(filter);
    }

    public synchronized void syncForEach(Consumer<T> action) {
        forEach(action);
    }

    public synchronized ArrayList<T> syncTakeAll() {
        ArrayList<T> copy = new ArrayList<>(this);
        clear();
        return copy;
    }
}
