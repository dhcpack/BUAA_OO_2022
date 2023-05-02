import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static HashMap<Integer, Integer> groupExceptionCounter = new HashMap<>();
    private static int totalExceptionCounter = 0;
    private int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        groupExceptionCounter.put(id, groupExceptionCounter.getOrDefault(id, 0) + 1);
        totalExceptionCounter++;
    }

    @Override
    public void print() {
        System.out.printf("egi-%d, %d-%d\n", totalExceptionCounter, id,
            groupExceptionCounter.get(id));
    }
}
