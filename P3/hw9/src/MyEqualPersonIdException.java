import com.oocourse.spec1.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static final Counter COUNTER = new Counter();
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        COUNTER.raiseException(id);
    }

    public void print() {
        System.out.printf("epi-%d, %d-%d\n", COUNTER.getTotalTimes(), id,
                COUNTER.getTimesOfId(id));
    }
}
