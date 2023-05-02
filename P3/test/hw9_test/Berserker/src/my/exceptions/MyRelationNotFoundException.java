package my.exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException
{
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int sum = 0;
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2)
    {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);

        if (COUNTER.containsKey(id1))
        {
            COUNTER.put(id1, COUNTER.get(id1) + 1);
        }
        else
        {
            COUNTER.put(id1, 1);
        }

        if (COUNTER.containsKey(id2))
        {
            COUNTER.put(id2, COUNTER.get(id2) + 1);
        }
        else
        {
            COUNTER.put(id2, 1);
        }
        sum++;
    }

    @Override
    public void print()
    {
        System.out.println("rnf-" +
                sum +
                ", " +
                id1 +
                "-" +
                COUNTER.get(id1) +
                ", " +
                id2 +
                "-" +
                COUNTER.get(id2));
    }
}
