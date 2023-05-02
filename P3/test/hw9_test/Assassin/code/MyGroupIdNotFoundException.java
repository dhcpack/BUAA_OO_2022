import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static HashMap<Integer, Integer> groupExceptionCounter = new HashMap<>();
    private static int totalExceptionCounter = 0;
    private int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        groupExceptionCounter.put(id, groupExceptionCounter.getOrDefault(id, 0) + 1);
        totalExceptionCounter++;
    }

    @Override
    public void print() {
        System.out.printf("ginf-%d, %d-%d\n", totalExceptionCounter, id,
            groupExceptionCounter.get(id));
    }
}
