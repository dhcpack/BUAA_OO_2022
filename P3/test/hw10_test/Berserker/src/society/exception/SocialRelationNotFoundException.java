package society.exception;

import com.oocourse.spec2.exceptions.RelationNotFoundException;
import society.counter.Counter;

public class SocialRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2;
    private static Counter counter = new Counter();

    public SocialRelationNotFoundException(int id1, int id2) {
        counter.addCount(id1);
        counter.addCount(id2);
        counter.addSumcnt();
        if (id1 > id2) {
            this.id1 = id2;
            this.id2 = id1;
        } else {
            this.id1 = id1;
            this.id2 = id2;
        }
    }

    @Override
    public void print() {
        //counter.addCount(id1);
        //counter.addCount(id2);
        //counter.addSumcnt();
        System.out.printf("rnf-%d, %d-%d, %d-%d%n",
                counter.getSumcnt(), id1, counter.getCount(id1), id2, counter.getCount(id2));
    }
}
