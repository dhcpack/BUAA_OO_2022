package exc;

import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        CNT.putIfAbsent(id, 0);
        CNT.replace(id, CNT.get(id) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("epi-" + sum + ", " + id + "-" + CNT.get(id));
    }
}
