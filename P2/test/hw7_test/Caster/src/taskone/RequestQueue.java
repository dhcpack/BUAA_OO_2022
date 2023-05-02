package taskone;

import com.oocourse.elevator3.PersonRequest;
import java.util.LinkedList;
import java.util.List;

public class RequestQueue extends LinkedList<PersonRequest> {
    public synchronized PersonRequest take() throws InterruptedException {
        if (isEmpty()) {
            wait();
            return poll();
        } else {
            return poll();
        }
    }

    public synchronized void put(PersonRequest personRequest) throws InterruptedException {
        add(personRequest);
        notifyAll();
    }

    public synchronized void drainTo(List<PersonRequest> list) throws InterruptedException {
        list.addAll(this.subList(0, size()));
        this.clear();
    }
}
