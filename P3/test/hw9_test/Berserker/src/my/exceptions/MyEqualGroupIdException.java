package my.exceptions;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException
{
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int sum = 0;
    private final int id;

    public MyEqualGroupIdException(int id)
    {
        if (COUNTER.containsKey(id))
        {
            COUNTER.put(id, COUNTER.get(id) + 1);
        }
        else
        {
            COUNTER.put(id, 1);
        }
        sum++;

        this.id = id;
    }

    @Override
    public void print()
    {
        System.out.println("egi-" + sum +
                ", " +
                id +
                "-" + COUNTER.get(id));
    }
}
