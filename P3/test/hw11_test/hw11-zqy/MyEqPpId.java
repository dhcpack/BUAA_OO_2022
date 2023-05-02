import com.oocourse.spec3.exceptions.EqualPersonIdException;

public class MyEqPpId extends EqualPersonIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqPpId(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("epi-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
