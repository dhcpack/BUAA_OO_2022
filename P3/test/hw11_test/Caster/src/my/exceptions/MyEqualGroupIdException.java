package my.exceptions;

import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static final ExceptionCounter EGI_COUNTER = new ExceptionCounter();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        EGI_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("egi-" + EGI_COUNTER.count()
                + ", " + id + "-" + EGI_COUNTER.get(id));
    }
}
