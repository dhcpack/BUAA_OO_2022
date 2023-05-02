import java.util.HashMap;

public class MyEqualGroupIdException extends com.oocourse.spec2.exceptions.EqualGroupIdException {
    private static int count = 0;
    private final int id;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();

    public MyEqualGroupIdException(int id) {
        count += 1;
        this.id = id;
        if (!COUNTS.containsKey(id)) {
            COUNTS.put(id, 0);
        }
        COUNTS.put(id, COUNTS.get(id) + 1);
    }

    @Override
    public void print() {
        System.out.println("egi-" + count + ", " + id + "-" + COUNTS.get(id));
    }
}
