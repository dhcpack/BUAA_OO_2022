import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static Count count = new Count();
    private int id;
    
    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("einf-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
