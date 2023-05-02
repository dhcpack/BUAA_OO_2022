import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        COUNT.merge(id, 1, Integer::sum);
        ++total;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + total + ", " + id + "-" + COUNT.get(id));
    }
}
