import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMsgIdNotFn extends MessageIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyMsgIdNotFn(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("minf-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
