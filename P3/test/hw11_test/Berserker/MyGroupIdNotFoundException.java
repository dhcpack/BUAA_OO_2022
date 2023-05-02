import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        COUNT.merge(id, 1, Integer::sum);
        ++total;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + total + ", " + id + "-" + COUNT.get(id));
    }
}
