import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Count count = new Count();
    private int id;
    
    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.add(id);
    }
    
    public void print() {
        System.out.printf("ginf-%d, %d-%d\n", count.getTotalCount(), id, count.getSpecCount(id));
    }
    
}
