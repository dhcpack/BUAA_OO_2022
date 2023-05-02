package myinstance.exceptions;

import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {

    private int id1;
    private int id2;

    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        numOfException++;
        exceptionMap.merge(id1, 1, (oldValue, newValue) -> oldValue + 1);
        if (id1 != id2) {
            exceptionMap.merge(id2, 1, (oldValue, newValue) -> oldValue + 1);
        }

    }

    @Override
    public void print() {
        System.out.println("er-" + numOfException + ", " +
                id1 + "-" + exceptionMap.get(id1) + ", " +
                id2 + "-" + exceptionMap.get(id2));
        /*System.out.printf("er-%d, %d-%d, %d-%d\n",
                numOfException,
                id1,
                exceptionMap.get(id1),
                id2,
                exceptionMap.get(id2));*/
    }
}
