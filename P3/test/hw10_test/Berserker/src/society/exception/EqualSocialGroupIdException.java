package society.exception;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import society.counter.Counter;

public class EqualSocialGroupIdException extends EqualGroupIdException {
    private int id;
    private static Counter counter = new Counter();

    public EqualSocialGroupIdException(int id) {
        this.id = id;
        counter.addCount(id);
        counter.addSumcnt();
    }

    @Override
    public void print() {
        //counter.addCount(id);
        //counter.addSumcnt();
        System.out.printf("egi-%d, %d-%d%n",
                counter.getSumcnt(), id, counter.getCount(id));
    }
}
