package implement.myexceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;
import implement.entity.Counter;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static Counter counter = new Counter();

    public MyRelationNotFoundException(int id1, int id2) {
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
        System.out.println(String.format("rnf-%d, %d-%d, %d-%d",
                counter.getTotal(), id1, counter.getCount(id1), id2, counter.getCount(id2)));
    }
}
