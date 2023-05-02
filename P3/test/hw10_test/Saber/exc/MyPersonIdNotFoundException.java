package exc;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        CNT.putIfAbsent(id, 0);
        CNT.replace(id, CNT.get(id) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + sum + ", " + id + "-" + CNT.get(id));
    }
}
