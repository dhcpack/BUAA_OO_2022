import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Count count = new Count();
    private int id;
    
    public MyEqualGroupIdException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("egi-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
