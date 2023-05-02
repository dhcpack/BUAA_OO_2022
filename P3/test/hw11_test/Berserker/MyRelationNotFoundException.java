import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;

    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int total = 0;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        COUNT.merge(id1, 1, Integer::sum);
        COUNT.merge(id2, 1, Integer::sum);
        total += 1;
    }

    @Override
    public void print() {
        int l = Integer.min(id1, id2);
        int r = Integer.max(id1, id2);
        System.out.println("rnf-" + total + ", " + l + "-" + COUNT.get(l) +
                ", " + r + "-" + COUNT.get(r));
    }
}
