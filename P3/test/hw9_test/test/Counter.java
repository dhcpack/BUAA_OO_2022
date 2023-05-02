import java.util.HashMap;

public class Counter {
    private final HashMap<Integer, Integer> exceptionTimes = new HashMap<>();
    private int totalTimes = 0;

    public void raiseException(int id) {
        raiseExceptionId(id);
        totalTimes++;
    }

    public void raiseException(int id1, int id2) {
        raiseExceptionId(id1);
        raiseExceptionId(id2);
        totalTimes++;
    }

    private void raiseExceptionId(int id) {
        if (exceptionTimes.containsKey(id)) {
            exceptionTimes.put(id, exceptionTimes.get(id) + 1);
        } else {
            exceptionTimes.put(id, 1);
        }
    }

    public int getTimesOfId(int id) {
        return exceptionTimes.get(id);
    }

    public int getTotalTimes() {
        return totalTimes;
    }
}
