import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

public class MyPpIdNotFn extends PersonIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyPpIdNotFn(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
