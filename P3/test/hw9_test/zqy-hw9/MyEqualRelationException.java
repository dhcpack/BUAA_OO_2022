import com.oocourse.spec1.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqualRelationException(int id1, int id2) {
        COUNTER.increase(id1, id2);
        // for printing
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
    }

    public void print() {
        System.out.println(String.format("er-%d, %d-%d, %d-%d", //
                COUNTER.getTotalCount(), //
                id1, COUNTER.getIdCount(id1), //
                id2, COUNTER.getIdCount(id2)));
    }
}
