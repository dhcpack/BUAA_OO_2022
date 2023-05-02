import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        COUNT.merge(id1, 1, Integer::sum);
        if (id1 == id2) {
            total += 1;
            return;
        }
        COUNT.merge(id2, 1,Integer::sum);
        total += 1;
    }

    @Override
    public void print() {
        int l = Integer.min(id1, id2);
        int r = Integer.max(id1, id2);
        System.out.println("er-" + total + ", " + l + "-" + COUNT.get(l) +
                ", " + r + "-" + COUNT.get(r));
    }

}
