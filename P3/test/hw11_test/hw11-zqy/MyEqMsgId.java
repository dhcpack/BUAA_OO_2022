import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqMsgId extends EqualMessageIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqMsgId(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("emi-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
