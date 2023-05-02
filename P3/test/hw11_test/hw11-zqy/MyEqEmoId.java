import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqEmoId extends EqualEmojiIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqEmoId(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("eei-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
