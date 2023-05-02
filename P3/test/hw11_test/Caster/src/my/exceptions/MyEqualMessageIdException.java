package my.exceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int id;
    private static final ExceptionCounter EMI_COUNTER = new ExceptionCounter();

    public MyEqualMessageIdException(int id) {
        this.id = id;
        EMI_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("emi-" + EMI_COUNTER.count()
                + ", " + id + "-" + EMI_COUNTER.get(id));
    }
}
