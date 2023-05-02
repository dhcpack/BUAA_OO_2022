package my.exceptions;

import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException
{
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int sum = 0;
    private final int id;

    public MyGroupIdNotFoundException(int id)
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
        System.out.println("ginf-" + sum +
                ", " +
                id +
                "-" + COUNTER.get(id));
    }
}
