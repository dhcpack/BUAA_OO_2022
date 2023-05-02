package exceptions;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static final Counter COUNTER = new Counter();
    private final int id;

    public MyEqualEmojiIdException(int id) {
        this.id = id;
        COUNTER.raiseException(id);
    }

    public void print() {
        System.out.printf("eei-%d, %d-%d\n", COUNTER.getTotalTimes(), id,
                COUNTER.getTimesOfId(id));
    }
}