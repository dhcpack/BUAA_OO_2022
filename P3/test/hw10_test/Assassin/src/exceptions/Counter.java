package exceptions;

import java.util.HashMap;

public class Counter {
    private int total;
    private HashMap<Integer, Integer> num;

    public Counter() {
        this.total = 0;
        this.num = new HashMap<>();
    }

    public int getNum() {
        return total;
    }

    public int getNum(int id) {
        return num.get(id);
    }

    public void add(int id) {
        if (!num.containsKey(id)) {
            num.put(id, 1);
        } else {
            num.replace(id, num.get(id) + 1);
        }
        total++;
    }

    public void add(int id1, int id2) {

    }

    public void subone() {
        total--;
    }
}
