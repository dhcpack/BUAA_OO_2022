import com.oocourse.spec3.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        COUNT.merge(id, 1, Integer::sum);
        ++total;
    }

    @Override
    public void print() {
        System.out.println("epi-" + total + ", " + id + "-" + COUNT.get(id));
    }
}
