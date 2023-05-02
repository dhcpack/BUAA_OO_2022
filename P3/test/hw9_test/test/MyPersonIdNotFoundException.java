import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter = new Counter();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        counter.raiseException(id);
    }

    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", counter.getTotalTimes(), id,
                counter.getTimesOfId(id));
    }
}
