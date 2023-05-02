import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static Count count = new Count();
    private int id;
    
    public MyEqualEmojiIdException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("eei-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
