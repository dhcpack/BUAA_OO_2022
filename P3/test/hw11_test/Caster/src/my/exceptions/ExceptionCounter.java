package my.exceptions;

import java.util.HashMap;

public class ExceptionCounter {
    private final HashMap<Integer, Integer> counter = new HashMap<>();
    private int count = 0;

    public int count() {
        return count;
    }

    public int get(int id) {
        if (!counter.containsKey(id)) {
            return 0;
        }
        return counter.get(id);
    }

    public void increase(int id) {
        counter.computeIfPresent(id, (key, value) -> value + 1);
        counter.putIfAbsent(id, 1);
        count++;
    }

    public void increase(int id1, int id2) {
        counter.computeIfPresent(id1, (key, value) -> value + 1);
        counter.putIfAbsent(id1, 1);
        if (id1 != id2) {
            counter.computeIfPresent(id2, (key, value) -> value + 1);
            counter.putIfAbsent(id2, 1);
        }
        count++;
    }
}
