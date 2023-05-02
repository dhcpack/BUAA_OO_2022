import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter = new Counter();
    private int tmp;
    
    public MyPersonIdNotFoundException(int id) {
        counter.addExceptionCount();
        counter.addIdCount(id);
        tmp = id;
    }
    
    public void print() {
        System.out.println("pinf-" + counter.getExceptionCount() +
                ", " + tmp + "-" + counter.getIdCount(tmp));
    }
}
