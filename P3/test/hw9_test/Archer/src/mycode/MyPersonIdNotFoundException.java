package mycode;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private static int sum = 0;
    private int id;

    public MyPersonIdNotFoundException(int id) {
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
        System.out.println("pinf-" + sum + ", " + id + "-" + map.get(id));
    }
}