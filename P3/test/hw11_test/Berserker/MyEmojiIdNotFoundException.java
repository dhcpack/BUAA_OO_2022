import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private final int id;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        COUNT.merge(id, 1, Integer::sum);
        ++total;
    }

    @Override
    public void print() {
        System.out.println("einf-" + total + ", " + id + "-" + COUNT.get(id));
    }
}
