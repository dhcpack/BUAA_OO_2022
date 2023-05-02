import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

public class MyGrpIdNotFn extends GroupIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyGrpIdNotFn(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("ginf-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
