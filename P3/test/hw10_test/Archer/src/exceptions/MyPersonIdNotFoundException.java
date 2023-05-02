package exceptions;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import exceptions.Counter;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;

        COUNTER.addCount(id);
        ++totalCounter;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));

    }
}
