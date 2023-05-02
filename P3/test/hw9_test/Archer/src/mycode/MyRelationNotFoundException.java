package mycode;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private static int sum = 0;
    private int id1;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        if (map.containsKey(id1)) {
            map.replace(id1, map.get(id1) + 1);
        } else {
            map.put(id1, 1);
        }
        if (map.containsKey(id2)) {
            map.replace(id2, map.get(id2) + 1);
        } else {
            map.put(id2, 1);
        }
        sum++;
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public void print() {
        if (id1 < id2) {
            System.out.println("rnf-" + sum + ", " + id1 +
                    "-" + map.get(id1) + ", " + id2 + "-" + map.get(id2));
        } else {
            System.out.println("rnf-" + sum + ", " + id2 +
                    "-" + map.get(id2) + ", " + id1 + "-" + map.get(id1));
        }
    }
}