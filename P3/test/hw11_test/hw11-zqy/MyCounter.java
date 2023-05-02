import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class MyCounter {
    private int total = 0;
    private final HashMap<Integer, Integer> byId = new HashMap<>();

    public int getTotal() {
        return total;
    }

    public int getById(int id) {
        return byId.getOrDefault(id, 0);
    }

    public void increase(Integer... ids) {
        total += 1;
        for (Integer id : new HashSet<Integer>(Arrays.asList(ids))) {
            byId.put(id, getById(id) + 1);
        }
    }
}
