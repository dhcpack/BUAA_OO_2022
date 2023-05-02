package implement.myexceptions;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import implement.entity.Counter;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        counter.add(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("epi-%d, %d-%d",
                counter.getTotal(), id, counter.getCount(id)));
    }
}
