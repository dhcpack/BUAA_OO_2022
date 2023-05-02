import java.util.HashMap;

public class MyEqualRelationException extends com.oocourse.spec2.exceptions.EqualRelationException {
    private final int id1;
    private final int id2;
    private static int count = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);

        count++;

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
    }

    @Override
    public void print() {
        System.out.println(
                "er-" + count + ", " + id1 + "-" + COUNTS.get(id1) + ", " + id2 + "-" +
                        COUNTS.get(id2));
    }
}
