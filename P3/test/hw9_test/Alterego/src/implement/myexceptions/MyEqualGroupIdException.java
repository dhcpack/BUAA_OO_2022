package implement.myexceptions;

import com.oocourse.spec1.exceptions.EqualGroupIdException;
import implement.entity.Counter;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        counter.add(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("egi-%d, %d-%d",
                counter.getTotal(), id, counter.getCount(id)));
    }
}
