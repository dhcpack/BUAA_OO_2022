package exceptions;

import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static final Counter COUNTER = new Counter();
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        COUNTER.raiseException(id);
    }

    public void print() {
        System.out.printf("egi-%d, %d-%d\n", COUNTER.getTotalTimes(), id,
                COUNTER.getTimesOfId(id));
    }
}
