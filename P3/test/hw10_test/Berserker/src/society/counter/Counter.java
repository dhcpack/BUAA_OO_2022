package society.counter;

import java.util.HashMap;

public class Counter {
    private HashMap<Integer, Integer> cnt;
    private int sumcnt;

    public Counter() {
        this.cnt = new HashMap<>();
        this.sumcnt = 0;
    }

    public void addCount(int id) {
        if (cnt.containsKey(id)) {
            int nowcount = cnt.get(id);
            cnt.put(id, nowcount + 1);
        } else {
            cnt.put(id, 1);
        }
    }

    public void addSumcnt() {
        sumcnt++;
    }

    public int getCount(int id) {
        return cnt.get(id);
    }

    public int getSumcnt() {
        return sumcnt;
    }
}
