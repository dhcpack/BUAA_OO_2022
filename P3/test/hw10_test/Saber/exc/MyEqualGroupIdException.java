package exc;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        CNT.putIfAbsent(id, 0);
        CNT.replace(id, CNT.get(id) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("egi-" + sum + ", " + id + "-" + CNT.get(id));
    }
}
