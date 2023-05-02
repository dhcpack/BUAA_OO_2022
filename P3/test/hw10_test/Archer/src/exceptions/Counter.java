package exceptions;

import java.util.HashMap;

public class Counter {
    private final HashMap<Integer, Integer> counter = new HashMap<>();

    public int getCount(int id) {
        if (counter.containsKey(id)) {
            return counter.get(id);
        }
        return 0;
    }

    public void addCount(int id) {
        if (!counter.containsKey(id)) {
            counter.put(id, 0);
        }
        counter.put(id, counter.get(id) + 1);
    }
}
