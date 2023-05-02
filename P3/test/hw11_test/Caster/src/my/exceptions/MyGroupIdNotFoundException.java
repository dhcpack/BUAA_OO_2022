package my.exceptions;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static final ExceptionCounter GINF_COUNTER = new ExceptionCounter();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        GINF_COUNTER.increase(id);
    }

    public void print() {
        System.out.println("ginf-" + GINF_COUNTER.count()
                + ", " + id + "-" + GINF_COUNTER.get(id));
    }
}
