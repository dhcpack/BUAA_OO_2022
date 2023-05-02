import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("minf-%d, %d-%d", //
                COUNTER.getTotalCount(), id, COUNTER.getIdCount(id)));
    }
}
