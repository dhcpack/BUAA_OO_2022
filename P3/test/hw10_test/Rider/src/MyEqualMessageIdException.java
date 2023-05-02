import java.util.HashMap;

public class MyEqualMessageIdException
        extends com.oocourse.spec2.exceptions.EqualMessageIdException {
    private static int count = 0;
    private int id;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();

    public MyEqualMessageIdException(int id) {
        count += 1;
        this.id = id;
        if (!COUNTS.containsKey(id)) {
            COUNTS.put(id, 0);
        }
        COUNTS.put(id, COUNTS.get(id) + 1);
    }

    @Override
    public void print() {
        System.out.println("emi-" + count + ", " + id + "-" + COUNTS.get(id));
    }

}
