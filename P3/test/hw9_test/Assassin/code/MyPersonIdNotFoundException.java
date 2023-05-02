import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static HashMap<Integer, Integer> personExceptionCounter = new HashMap<>();
    private static int totalExceptionCounter = 0;
    private int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        personExceptionCounter.put(id, personExceptionCounter.getOrDefault(id, 0) + 1);
        totalExceptionCounter++;
    }

    @Override
    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", totalExceptionCounter, id,
            personExceptionCounter.get(id));
    }
}
