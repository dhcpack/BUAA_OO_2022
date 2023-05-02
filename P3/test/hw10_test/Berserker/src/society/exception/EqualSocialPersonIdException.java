package society.exception;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import society.counter.Counter;

public class EqualSocialPersonIdException extends EqualPersonIdException {
    private int id;
    private static Counter counter = new Counter();

    public EqualSocialPersonIdException(int id) {
        counter.addCount(id);
        counter.addSumcnt();
        this.id = id;
    }

    @Override
    public void print() {
        //counter.addCount(id);
        //counter.addSumcnt();
        System.out.printf("epi-%d, %d-%d%n",
                counter.getSumcnt(), id, counter.getCount(id));
    }
}
