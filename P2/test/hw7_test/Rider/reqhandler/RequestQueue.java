package src.reqhandler;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestQueue {

    private final ConcurrentLinkedQueue<MyPersonRequest> requestQueue;
    private boolean reachEnd;

    public ConcurrentLinkedQueue<MyPersonRequest> getRequestQueue() {
        return requestQueue;
    }

    public RequestQueue() {
        this.requestQueue = new ConcurrentLinkedQueue<>();
        this.reachEnd = false;
    }

    public synchronized int size() {
        return requestQueue.size();
    }

    public synchronized MyPersonRequest poll() {
        notifyAll();
        return this.requestQueue.poll();
    }

    public synchronized void addRequest(MyPersonRequest request) {
        this.requestQueue.add(request);
        notifyAll();
    }

    public synchronized void setEnd(boolean reachEnd) {
        this.reachEnd = reachEnd;
        notifyAll();
    }

    public synchronized boolean isReachEnd() {
        return this.reachEnd;
    }

    public synchronized boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    public synchronized MyPersonRequest peek() {
        return requestQueue.peek();
    }

}