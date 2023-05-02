package mycode;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {

    private static HashMap<Integer, Integer> map = new HashMap<>();
    private static int sum = 0;
    private int id;

    public MyEqualGroupIdException(int id) {
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
        System.out.println("egi-" + sum + ", " + id + "-" + map.get(id));
    }
}