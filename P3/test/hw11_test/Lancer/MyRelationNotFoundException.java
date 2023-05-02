import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int a, int b) {
        count++;
        this.id1 = Integer.min(a, b);
        this.id2 = Integer.max(a, b);
        //print id1 first
        if (!idCounter.containsKey(this.id1)) {
            idCounter.put(this.id1, 1);
        } else {
            int prev = idCounter.get(this.id1);
            idCounter.put(this.id1, prev + 1);
        }
        //if id1 != id2
        if (this.id2 > this.id1) {
            if (!idCounter.containsKey(this.id2)) {
                idCounter.put(this.id2, 1);
            } else {
                int prev = idCounter.get(this.id2);
                idCounter.put(this.id2, prev + 1);
            }
        }
    }

    @Override
    public void print() {
        System.out.println(String.format("rnf-%d, %d-%d, %d-%d",
                count, id1, idCounter.get(id1), id2, idCounter.get(id2)));
    }
}
