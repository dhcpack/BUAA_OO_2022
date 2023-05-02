import com.oocourse.spec2.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static final Counter COUNTER = new Counter();
    private int id1;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        COUNTER.raiseException(id1, id2);
    }

    public void print() {
        if (id1 > id2) {
            int temp = id1;
            id1 = id2;
            id2 = temp;
        }
        System.out.printf("rnf-%d, %d-%d, %d-%d\n", COUNTER.getTotalTimes(), id1,
                COUNTER.getTimesOfId(id1), id2, COUNTER.getTimesOfId(id2));
    }
}
