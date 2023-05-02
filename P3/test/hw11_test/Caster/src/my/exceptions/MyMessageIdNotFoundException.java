package my.exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int id;
    private static final ExceptionCounter MINF_COUNTER = new ExceptionCounter();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        MINF_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("minf-" + MINF_COUNTER.count()
                + ", " + id + "-" + MINF_COUNTER.get(id));
    }
}
