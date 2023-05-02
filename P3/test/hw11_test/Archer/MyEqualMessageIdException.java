import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static Count count = new Count();
    private int id;
    
    public MyEqualMessageIdException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("emi-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
