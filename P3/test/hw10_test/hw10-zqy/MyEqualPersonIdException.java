import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("epi-%d, %d-%d", //
                COUNTER.getTotalCount(), id, COUNTER.getIdCount(id)));
    }
}
