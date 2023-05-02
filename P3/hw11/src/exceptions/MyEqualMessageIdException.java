package exceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static final Counter COUNTER = new Counter();
    private final int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        COUNTER.raiseException(id);
    }

    public void print() {
        System.out.printf("emi-%d, %d-%d\n", COUNTER.getTotalTimes(), id,
                COUNTER.getTimesOfId(id));
    }

}