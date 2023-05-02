import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static final MyCounter COUNTER = new MyCounter();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        COUNTER.increase(id);
    }

    public void print() {
        System.out.println(String.format("ginf-%d, %d-%d", //
                COUNTER.getTotalCount(), id, COUNTER.getIdCount(id)));
    }
}
