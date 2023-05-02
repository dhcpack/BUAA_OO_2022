import java.util.HashMap;

public class MyRelationNotFoundException
        extends com.oocourse.spec2.exceptions.RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();
    private static int count = 0;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        if (!COUNTS.containsKey(id1)) {
            COUNTS.put(id1, 0);
        }
        if (!COUNTS.containsKey(id2)) {
            COUNTS.put(id2, 0);
        }

        COUNTS.put(id1, COUNTS.get(id1) + 1);
        if (id1 != id2) {
            COUNTS.put(id2, COUNTS.get(id2) + 1);
        }
        count += 1;
    }

    @Override
    public void print() {
        System.out.println("rnf-" + count + ", " + id1 + "-" + COUNTS.get(id1) + ", " + id2 + "-" +
                COUNTS.get(id2));
    }
}
