import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (!idCounter.containsKey(id)) {
            idCounter.put(id, 1);
        } else {
            Integer prev = idCounter.get(id);
            idCounter.put(id, prev + 1);
        }
    }

    @Override
    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d", count, id, idCounter.get(id)));
    }
}
