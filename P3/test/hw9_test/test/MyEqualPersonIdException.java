import com.oocourse.spec1.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        counter.raiseException(id);
    }

    public void print() {
        System.out.printf("epi-%d, %d-%d\n", counter.getTotalTimes(), id,
                counter.getTimesOfId(id));
    }

}
