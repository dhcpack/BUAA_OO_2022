package my.exceptions;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private final int id;
    private static final ExceptionCounter EEI_COUNTER = new ExceptionCounter();

    public MyEqualEmojiIdException(int id) {
        this.id = id;
        EEI_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("eei-" + EEI_COUNTER.count()
                + ", " + id + "-" + EEI_COUNTER.get(id));
    }
}
