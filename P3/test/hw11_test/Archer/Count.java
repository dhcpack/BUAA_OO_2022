import java.util.HashMap;

public class Count {
    private int count;
    private HashMap<Integer, Integer> time = new HashMap<>();
    
    public Count() {
        this.count = 0;
    }
    
    public void addSum() {
        this.count += 1;
    }
    
    public void add(int id) {
        if (this.time.containsKey(id)) {
            this.time.put(id, this.time.get(id) + 1);
        } else {
            this.time.put(id, 1);
        }
    }
    
    public int getTotalCount() {
        return count;
    }
    
    public int getSpecCount(int id) {
        return time.get(id);
    }
}
