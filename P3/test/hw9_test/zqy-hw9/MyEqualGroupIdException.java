import com.oocourse.spec1.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("egi-%d, %d-%d", //
                COUNTER.getTotalCount(), id, COUNTER.getIdCount(id)));
    }
}
