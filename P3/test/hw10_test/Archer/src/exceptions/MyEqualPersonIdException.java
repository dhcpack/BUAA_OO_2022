package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import exceptions.Counter;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        COUNTER.addCount(id);
        ++totalCounter;
    }

    @Override
    public void print() {
        System.out.println("epi-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));

    }

}
