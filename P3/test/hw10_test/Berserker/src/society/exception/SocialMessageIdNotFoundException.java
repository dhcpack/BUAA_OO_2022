package society.exception;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import society.counter.Counter;

public class SocialMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static Counter counter = new Counter();

    public SocialMessageIdNotFoundException(int id) {
        this.id = id;
        counter.addCount(id);
        counter.addSumcnt();
    }

    @Override
    public void print() {
        System.out.printf("minf-%d, %d-%d%n", counter.getSumcnt(), id, counter.getCount(id));
    }
}
