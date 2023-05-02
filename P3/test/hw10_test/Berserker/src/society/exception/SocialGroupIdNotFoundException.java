package society.exception;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import society.counter.Counter;

public class SocialGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id;
    private static Counter counter = new Counter();

    public SocialGroupIdNotFoundException(int id) {
        counter.addCount(id);
        counter.addSumcnt();
        this.id = id;
    }

    @Override
    public void print() {
        //counter.addCount(id);
        //counter.addSumcnt();
        System.out.printf("ginf-%d, %d-%d%n",
                counter.getSumcnt(), id, counter.getCount(id));
    }
}
