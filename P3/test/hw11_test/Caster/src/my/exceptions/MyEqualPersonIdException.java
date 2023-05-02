package my.exceptions;

import com.oocourse.spec3.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static final ExceptionCounter EPI_COUNTER = new ExceptionCounter();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        EPI_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("epi-" + EPI_COUNTER.count()
                + ", " + id + "-" + EPI_COUNTER.get(id));
    }
}
