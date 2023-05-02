import com.oocourse.spec1.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private int tmp;
    
    public MyEqualPersonIdException(int id) {
        counter.addExceptionCount();
        counter.addIdCount(id);
        tmp = id;
    }
    
    public void print() {
        System.out.println("epi-" + counter.getExceptionCount() +
                ", " + tmp + "-" + counter.getIdCount(tmp));
    }
}
