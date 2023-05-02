import java.util.HashMap;

public class MyEqualPersonIdException extends com.oocourse.spec2.exceptions.EqualPersonIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        count += 1;
        if (!COUNTS.containsKey(id)) {
            COUNTS.put(id, 0);
        }
        COUNTS.put(id, COUNTS.get(id) + 1);
    }

    @Override
    public void print() {
        System.out.println("epi-" + count + ", " + id + "-" + COUNTS.get(id));
    }
}