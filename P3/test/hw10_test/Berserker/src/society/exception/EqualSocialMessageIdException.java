package society.exception;

import com.oocourse.spec2.exceptions.EqualMessageIdException;
import society.counter.Counter;

public class EqualSocialMessageIdException extends EqualMessageIdException {
    private int id;
    private static Counter counter = new Counter();

    public EqualSocialMessageIdException(int id) {
        this.id = id;
        counter.addCount(id);
        counter.addSumcnt();
    }

    @Override
    public void print() {
        System.out.printf("emi-%d, %d-%d%n", counter.getSumcnt(), id, counter.getCount(id));
    }
}
