import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static Count count = new Count();
    private int id;
    
    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("minf-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
