import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmoIdNotFn extends EmojiIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEmoIdNotFn(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("einf-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
