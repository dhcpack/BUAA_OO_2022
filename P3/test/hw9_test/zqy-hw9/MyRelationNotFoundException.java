import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static final MyCounter COUNTER = new MyCounter();

    public MyRelationNotFoundException(int id1, int id2) {
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
        System.out.println(String.format("rnf-%d, %d-%d, %d-%d", //
                COUNTER.getTotalCount(), //
                id1, COUNTER.getIdCount(id1), //
                id2, COUNTER.getIdCount(id2)));
    }
}
