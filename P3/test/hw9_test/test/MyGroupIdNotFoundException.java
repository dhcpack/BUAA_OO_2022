import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Counter counter = new Counter();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        counter.raiseException(id);
    }

    public void print() {
        System.out.printf("ginf-%d, %d-%d\n", counter.getTotalTimes(), id,
                counter.getTimesOfId(id));
    }
}
