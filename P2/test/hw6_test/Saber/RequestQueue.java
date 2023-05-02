import java.util.ArrayList;

public class RequestQueue {
    private ArrayList<MoveRequest> moveRequests;
    private boolean isEnd;
    private int length;

    public RequestQueue() {
        moveRequests = new ArrayList<>();
        this.isEnd = false;
        length = 0;
    }

    public synchronized void addRequest(MoveRequest moveRequest) {
        moveRequests.add(moveRequest);
        length++;
        notifyAll();
    }

    public synchronized MoveRequest getOneRequest() {
        while (moveRequests.isEmpty() && !isEnd) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (moveRequests.isEmpty()) {
            return null;
        }
        length--;
        MoveRequest moveRequest = moveRequests.get(0);
        moveRequests.remove(0);
        notifyAll();
        return moveRequest;
    }

    public synchronized int getLength() {
        notifyAll();
        return this.length;
    }

    public synchronized boolean remove(MoveRequest m) {
        notifyAll();
        return this.moveRequests.remove(m);
    }

    public synchronized MoveRequest peek() {
        while (moveRequests.isEmpty() && !isEnd) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (moveRequests.isEmpty()) {
            return null;
        }
        MoveRequest moveRequest = moveRequests.get(0);
        notifyAll();
        return moveRequest;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return moveRequests.isEmpty();
    }

    public synchronized ArrayList<MoveRequest> getRequests() {
        ArrayList<MoveRequest> l = new ArrayList<>();
        for (MoveRequest t : moveRequests) {
            l.add(t.clone());
        }
        notifyAll();
        return l;
    }

    public synchronized MoveRequest get(int i) {
        notifyAll();
        return moveRequests.get(i);
    }

    public synchronized void del(int i) {
        moveRequests.remove(i);
        length--;
        notifyAll();
    }

}
