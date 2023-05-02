package implement.myexceptions;

import com.oocourse.spec1.exceptions.EqualRelationException;
import implement.entity.Counter;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static Counter counter = new Counter();

    public MyEqualRelationException(int id1, int id2) {
        if (id1 == id2) {
            this.id1 = this.id2 = id1;
            counter.add(id1);
        }
        else {
            counter.add(id1);
            counter.add(id2);
            counter.decreaseTotal();
            this.id1 = Math.min(id1, id2);
            this.id2 = Math.max(id1, id2);
        }
    }

    @Override
    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d\n",
                counter.getTotal(), id1, counter.getCount(id1), id2, counter.getCount(id2));
    }
}
