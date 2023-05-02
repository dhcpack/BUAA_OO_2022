package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyEqualGroupIdException(int id) {
        COUNTER.addCount(id);
        ++totalCounter;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("egi-" + totalCounter + ", " + id + "-" + COUNTER.getCount(id));
    }
}
