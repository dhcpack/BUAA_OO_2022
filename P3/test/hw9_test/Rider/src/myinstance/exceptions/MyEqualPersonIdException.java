package myinstance.exceptions;

import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {

    private final int id;
    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        numOfException++;
        exceptionMap.merge(id, 1, (oldValue, newValue) -> oldValue + 1);
        /*if (exceptionMap.containsKey(this.id)) {
            exceptionMap.put(id, 1);
        } else {
            int times = exceptionMap.get(id);
            exceptionMap.replace(id, times + 1);
        }*/
    }

    @Override
    public void print() {
        System.out.println("epi-" + numOfException + ", " + id + "-" + exceptionMap.get(id));
        // System.out.printf("epi-%d, %d-%d\n", numOfException, id, exceptionMap.get(id));
    }
}
