package myinstance.exceptions;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {

    private final int id;
    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
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
        System.out.println("pinf-" + numOfException + ", " + id + "-" + exceptionMap.get(id));
        // System.out.printf("pinf-%d, %d-%d\n", numOfException, id, exceptionMap.get(id));
    }
}
