import java.util.ArrayList;

public class RequestQueue {
    private ArrayList<Request> requests;
    private boolean isEnd;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized Request takeRequest() {
        if (requests.isEmpty() && !isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized Request getRequest(int i) {
        return requests.get(i);
    }

    public synchronized ArrayList<Request> getRequests() {
        return requests;
    }

    public synchronized int getSize() {
        return requests.size();
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
        return requests.isEmpty();
    }
}
