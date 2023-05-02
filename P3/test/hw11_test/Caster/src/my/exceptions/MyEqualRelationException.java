package my.exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static final ExceptionCounter ER_COUNTER = new ExceptionCounter();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        ER_COUNTER.increase(id1, id2);
    }

    public void print() {
        System.out.println("er-" + ER_COUNTER.count()
                + ", " + id1 + "-" + ER_COUNTER.get(id1)
                + ", " + id2 + "-" + ER_COUNTER.get(id2));
    }
}
