package mycode;

import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private static int sum = 0;
    private int id;

    public MyGroupIdNotFoundException(int id) {
        if (map.containsKey(id)) {
            map.replace(id, map.get(id) + 1);
        } else {
            map.put(id, 1);
        }
        sum++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + sum + ", " + id + "-" + map.get(id));
    }
}