import com.oocourse.spec1.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        counter.raiseException(id);
    }

    public void print() {
        System.out.printf("egi-%d, %d-%d\n", counter.getTotalTimes(), id,
                counter.getTimesOfId(id));
    }
}
