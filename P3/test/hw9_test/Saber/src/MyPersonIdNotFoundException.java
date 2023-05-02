import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static final Counter COUNTER = new Counter();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        COUNTER.raiseException(id);
    }

    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", COUNTER.getTotalTimes(), id,
                COUNTER.getTimesOfId(id));
    }
}
