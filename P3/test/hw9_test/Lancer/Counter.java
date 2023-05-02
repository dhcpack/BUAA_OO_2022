import java.util.HashMap;

public class Counter {
    private int exceptionCount;
    private final HashMap<Integer, Integer> idCount;
    
    public Counter() {
        exceptionCount = 0;
        idCount = new HashMap<>();
    }
    
    public void addExceptionCount() {
        exceptionCount++;
    }
    
    public void addIdCount(int id) {
        if (idCount.containsKey(id)) {
            idCount.replace(id, idCount.get(id) + 1);
        } else {
            idCount.put(id, 1);
        }
    }
    
    public int getExceptionCount() {
        return exceptionCount;
    }
    
    public int getIdCount(int id) {
        return idCount.get(id);
    }
}
