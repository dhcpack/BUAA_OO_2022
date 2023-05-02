import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Count count = new Count();
    private int id;
    
    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
}
