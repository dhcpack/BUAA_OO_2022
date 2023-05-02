package my.exceptions;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private final int id;
    private static final ExceptionCounter EINF_COUNTER = new ExceptionCounter();

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        EINF_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("einf-" + EINF_COUNTER.count()
                + ", " + id + "-" + EINF_COUNTER.get(id));
    }
}
