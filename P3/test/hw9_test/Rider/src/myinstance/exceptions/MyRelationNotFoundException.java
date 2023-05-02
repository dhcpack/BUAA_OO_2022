package myinstance.exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {

    private int id1;
    private int id2;

    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        numOfException++;
        /*if (exceptionMap.containsKey(id1)) {
            exceptionMap.put(id1, 1);
        } else {
            int times = exceptionMap.get(id1);
            exceptionMap.replace(id1, times + 1);
        }*/
        exceptionMap.merge(id1, 1, (oldValue, newValue) -> oldValue + 1);
        exceptionMap.merge(id2, 1, (oldValue, newValue) -> oldValue + 1);

        /*if (exceptionMap.containsKey(id2)) {
            exceptionMap.put(id2, 1);
        } else {
            int times = exceptionMap.get(id2);
            exceptionMap.replace(id2, times + 1);
        }*/

    }

    @Override
    public void print() {
        System.out.println("rnf-" + numOfException + ", " +
                id1 + "-" + exceptionMap.get(id1) + ", " +
                id2 + "-" + exceptionMap.get(id2));
        /*System.out.printf("rnf-%d, %d-%d, %d-%d\n",
                numOfException,
                id1,
                exceptionMap.get(id1),
                id2,
                exceptionMap.get(id2));*/
    }
}
