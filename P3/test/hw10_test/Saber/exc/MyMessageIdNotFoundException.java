package exc;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int id;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        CNT.putIfAbsent(id, 0);
        CNT.replace(id, CNT.get(id) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("minf-" + sum + ", " + id + "-" + CNT.get(id));
    }
}
