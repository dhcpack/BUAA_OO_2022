package implement.myexceptions;

import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import implement.entity.Counter;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static Counter COUNTER = new Counter();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        COUNTER.add(id);
    }

    @Override
    public void print() {
        System.out.printf("ginf-%d, %d-%d\n",
                COUNTER.getTotal(), id, COUNTER.getCount(id));
    }
}
