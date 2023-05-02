import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyEqGrpId extends EqualGroupIdException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyEqGrpId(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("egi-%d, %d-%d", //
                COUNTER.getTotal(), id, COUNTER.getById(id)));
    }
}
