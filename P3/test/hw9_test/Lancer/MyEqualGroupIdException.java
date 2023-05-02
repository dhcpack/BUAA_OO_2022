import com.oocourse.spec1.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private int tmp;
    
    public MyEqualGroupIdException(int id) {
        counter.addExceptionCount();
        counter.addIdCount(id);
        tmp = id;
    }
    
    public void print() {
        System.out.println("egi-" + counter.getExceptionCount() +
                ", " + tmp + "-" + counter.getIdCount(tmp));
    }
}
