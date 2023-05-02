import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static HashMap<Integer, Integer> personExceptionCounter = new HashMap<>();
    private static int totalExceptionCounter = 0;
    private int id1;
    private int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        totalExceptionCounter++;
        if (id1 == id2) {
            personExceptionCounter.put(id1, personExceptionCounter.getOrDefault(id1, 0) + 1);
        } else {
            personExceptionCounter.put(id1, personExceptionCounter.getOrDefault(id1, 0) + 1);
            personExceptionCounter.put(id2, personExceptionCounter.getOrDefault(id2, 0) + 1);
        }
    }

    @Override
    public void print() {
        if (id1 > id2) {
            int tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        System.out.printf("er-%d, %d-%d, %d-%d\n", totalExceptionCounter, id1,
            personExceptionCounter.get(id1), id2, personExceptionCounter.get(id2));
    }
}
