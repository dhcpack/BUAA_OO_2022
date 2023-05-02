package my.exceptions;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static final ExceptionCounter PINF_COUNTER = new ExceptionCounter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        PINF_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("pinf-" + PINF_COUNTER.count()
                + ", " + id + "-" + PINF_COUNTER.get(id));
    }
}
