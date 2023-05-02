import java.util.ArrayList;
import com.oocourse.elevator2.PersonRequest;

public class RequestQueue {
    private final ArrayList<PersonRequest> requests;
    private boolean isEnd;

    public RequestQueue() {
        requests = new ArrayList<>();
        this.isEnd = false;
    }

    public ArrayList<PersonRequest> getRequests() {
        return requests;
    }

    public synchronized void addRequest(PersonRequest request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized void waitRequest() {
        if (!isEnd && requests.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }
}
