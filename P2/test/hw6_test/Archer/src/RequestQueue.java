import java.util.ArrayList;

/**
 * thread safe elevator schedule
 */
public class RequestQueue {
    private final ArrayList<MyRequest> requests;
    private boolean noMoreInput;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.noMoreInput = false;
    }

    public synchronized ArrayList<MyRequest> getRequest() {
        return new ArrayList<>(requests);
    }

    public synchronized void watchForRequest() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized int size() {
        return requests.size();
    }

    public synchronized void addRequest(MyRequest request) {
        requests.add(request);
        this.notifyAll();
    }

    public synchronized void removeRequest(MyRequest request) {
        requests.remove(request);
        this.notifyAll();
    }

    public synchronized boolean isEmpty() {
        return this.requests.isEmpty();
    }

    public synchronized boolean isFinish() {
        return noMoreInput && requests.isEmpty();
    }

    public synchronized void setNoMoreInput(boolean noMoreInput) {
        this.noMoreInput = noMoreInput;
        this.notifyAll();
    }
}
