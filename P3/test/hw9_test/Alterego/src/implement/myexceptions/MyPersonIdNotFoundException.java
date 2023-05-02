package implement.myexceptions;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import implement.entity.Counter;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static Counter counter = new Counter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        counter.add(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d",
                counter.getTotal(), id, counter.getCount(id)));
    }
}
