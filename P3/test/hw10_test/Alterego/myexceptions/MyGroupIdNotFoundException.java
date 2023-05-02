package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int type = 4;
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("ginf-" + x + ", " + id + "-" + y);
    }
}
