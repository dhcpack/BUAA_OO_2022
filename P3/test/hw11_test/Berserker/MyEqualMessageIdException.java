import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int id;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        COUNT.merge(id, 1, Integer::sum);
        ++total;
    }

    @Override
    public void print() {
        System.out.println("emi-" + total + ", " + id + "-" + COUNT.get(id));
    }
}
