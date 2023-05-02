package myinstance.exceptions;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {

    private final int id;
    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        numOfException++;
        exceptionMap.merge(id, 1, (oldValue, newValue) -> oldValue + 1);
    }

    @Override
    public void print() {
        System.out.println("egi-" + numOfException + ", " + id + "-" + exceptionMap.get(id));
        // System.out.printf("egi-%d, %d-%d\n", numOfException, id, exceptionMap.get(id));
    }
}
