package my.exceptions;

import com.oocourse.spec3.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static final ExceptionCounter RNF_COUNTER = new ExceptionCounter();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        RNF_COUNTER.increase(id1, id2);
    }

    public void print() {
        System.out.println("rnf-" + RNF_COUNTER.count()
                + ", " + id1 + "-" + RNF_COUNTER.get(id1)
                + ", " + id2 + "-" + RNF_COUNTER.get(id2));
    }
}
