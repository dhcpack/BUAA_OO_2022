package exceptions;

import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {

    private final int id;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyEqualMessageIdException(int id) {
        COUNTER.addCount(id);
        ++totalCounter;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("emi-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));
    }

}
