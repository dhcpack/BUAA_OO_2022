import com.oocourse.elevator1.PersonRequest;

import java.util.LinkedList;
import java.util.Queue;

public class RequestTransfer {
    private Queue<PersonRequest> personRequestQueue = new LinkedList<>();
    private boolean end;

    public synchronized void inputRequest(PersonRequest personRequest) {
        personRequestQueue.offer(personRequest);
        notifyAll();
    }

    public synchronized PersonRequest outputRequest() {
        if (personRequestQueue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return personRequestQueue.poll();
    }

    public synchronized void setEnd(boolean end) {
        this.end = end;
        notifyAll();
    }

    public boolean hasRequest() {
        return personRequestQueue.size() != 0;
    }

    public boolean isEnd() {
        return end;
    }
}
