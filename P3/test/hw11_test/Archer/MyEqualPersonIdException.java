import com.oocourse.spec3.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Count count = new Count();
    private int id;
    
    public MyEqualPersonIdException(int id) {
        count.addSum();
        this.id = id;
        count.add(id);
    }
    
    public void print() {
        System.out.printf("epi-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
