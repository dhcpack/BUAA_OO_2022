import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Counter counter = new Counter();
    private int tmp;
    
    public MyGroupIdNotFoundException(int id) {
        counter.addExceptionCount();
        counter.addIdCount(id);
        tmp = id;
    }
    
    public void print() {
        System.out.println("ginf-" + counter.getExceptionCount() +
                ", " + tmp + "-" + counter.getIdCount(tmp));
    }
}
