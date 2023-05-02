package exc;

import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static final HashMap<Integer, Integer> CNT = new HashMap<>();
    private static int sum = 0;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        CNT.putIfAbsent(id1, 0);
        CNT.putIfAbsent(id2, 0);
        CNT.replace(id1, CNT.get(id1) + 1);
        CNT.replace(id2, CNT.get(id2) + 1);
        sum++;
    }

    @Override
    public void print() {
        System.out.println("rnf-" + sum
                + ", " + id1 + "-" + CNT.get(id1)
                + ", " + id2 + "-" + CNT.get(id2));
    }
}
