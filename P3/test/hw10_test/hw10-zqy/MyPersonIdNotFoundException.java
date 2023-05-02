import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d", //
                COUNTER.getTotalCount(), id, COUNTER.getIdCount(id)));
    }
}
