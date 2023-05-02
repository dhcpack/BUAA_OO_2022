package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int type = 7;
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        CalFre.getInstance().updateCal(type, id1, true);
        CalFre.getInstance().updateCal(type, id2, false);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y1 = CalFre.getInstance().queryId(type, id1);
        int y2 = CalFre.getInstance().queryId(type, id2);
        if (id1 <= id2) {
            System.out.println("rnf-" + x + ", " + id1 + "-" + y1 + ", " + id2 + "-" + y2);
        }
        else {
            System.out.println("rnf-" + x + ", " + id2 + "-" + y2 + ", " + id1 + "-" + y1);
        }
    }
}
