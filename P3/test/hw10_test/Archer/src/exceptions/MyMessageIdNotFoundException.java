package exceptions;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {

    private final int id;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyMessageIdNotFoundException(int id) {
        COUNTER.addCount(id);
        ++totalCounter;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("minf-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));
    }
}
