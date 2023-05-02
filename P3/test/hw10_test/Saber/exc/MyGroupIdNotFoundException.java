package exc;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        CNT.putIfAbsent(id, 0);
        CNT.replace(id, CNT.get(id) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + sum + ", " + id + "-" + CNT.get(id));
    }

}
