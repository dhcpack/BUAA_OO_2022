import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyQueue<T> extends ArrayList<T> {
    public MyQueue() {
        super();
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
            wait();
        }
        return !isEmpty();
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
