package exceptions;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import exceptions.Counter;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;

    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyGroupIdNotFoundException(int id) {

        COUNTER.addCount(id);
        ++totalCounter;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));

    }
}
