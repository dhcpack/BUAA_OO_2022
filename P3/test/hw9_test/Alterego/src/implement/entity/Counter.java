package implement.entity;

import java.util.HashMap;

public class Counter {
    private HashMap<Integer, Integer> countMap;
    private int total = 0;

    public Counter() {
        this.countMap = new HashMap<>();
    }

    public void add(int id) {
        if (countMap.containsKey(id)) {
            int newValue = countMap.get(id) + 1;
            countMap.replace(id, newValue);
        }
        else {
            countMap.put(id, 1);
        }
        total += 1;
    }

    public void decreaseTotal() {
        total -= 1;
    }

    public int getCount(int id) {
        return countMap.get(id);
    }

    public int getTotal() {
        return total;
    }
}
