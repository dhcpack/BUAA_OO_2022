import java.util.HashMap;

public class MyCounter {
    private int countTotal = 0;
    private final HashMap<Integer, Integer> countsById = new HashMap<>();

    public int getTotalCount() {
        return countTotal;
    }

    public int getIdCount(int id) {
        return countsById.getOrDefault(id, 0);
    }

    public void increase(int id) {
        countTotal++;
        countsById.put(id, getIdCount(id) + 1);
    }

    public void increase(int id1, int id2) {
        if (id1 == id2) {
            countsById.put(id1, getIdCount(id1) + 1);
        } else {
            countsById.put(id1, getIdCount(id1) + 1);
            countsById.put(id2, getIdCount(id2) + 1);
        }
        countTotal++;
    }
}
