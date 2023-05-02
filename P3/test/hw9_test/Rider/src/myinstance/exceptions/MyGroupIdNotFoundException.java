package myinstance.exceptions;

import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {

    private final int id;
    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        numOfException++;
        exceptionMap.merge(id, 1, (oldValue, newValue) -> oldValue + 1);
    }

    @Override
    public void print() {
        System.out.println("ginf-" + numOfException + ", " + id + "-" + exceptionMap.get(id));
        // System.out.printf("ginf-%d, %d-%d\n", numOfException, id, exceptionMap.get(id));
    }
}
