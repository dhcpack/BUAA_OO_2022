package exceptions;

import com.oocourse.spec2.exceptions.RelationNotFoundException;
import exceptions.Counter;

public class MyRelationNotFoundException extends RelationNotFoundException {

    private final int id1;
    private final int id2;
    private static int totalCounter = 0;
    private static final Counter COUNTER = new Counter();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);

        COUNTER.addCount(id1);
        if (id1 != id2) {
            // 有可能id1 id2是同一个，这个时候只要加一遍就行了
            COUNTER.addCount(id2);
        }
        ++totalCounter;
    }

    @Override
    public void print() {

        System.out.println("rnf-" + totalCounter + ", " +
                id1 + "-" + COUNTER.getCount(id1) + ", " +
                id2 + "-" + COUNTER.getCount(id2));
    }
}