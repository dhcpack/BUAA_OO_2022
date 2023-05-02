package exceptions;

import com.oocourse.spec2.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private static Counter counter = new Counter();
    private int id1;
    private int id2;

    public MyEqualRelationException(int id1, int id2) {
        counter.add(id1);
        if (id1 != id2) {
            counter.add(id2);
            counter.subone();
        }
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
    }

    @Override
    public void print() {
        System.out.println("er-" + counter.getNum() +
                ", " + id1 + "-" + counter.getNum(id1) +
                ", " + id2 + "-" + counter.getNum(id2));
    }
}
