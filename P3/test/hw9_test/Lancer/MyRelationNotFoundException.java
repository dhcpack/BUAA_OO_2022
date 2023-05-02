import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static Counter counter = new Counter();
    private int tmp1;
    private int tmp2;
    
    public MyRelationNotFoundException(int id1, int id2) {
        counter.addExceptionCount();
        counter.addIdCount(id1);
        counter.addIdCount(id2);
        tmp1 = id1;
        tmp2 = id2;
    }
    
    public void print() {
        System.out.print("rnf-" + counter.getExceptionCount());
        if (tmp1 < tmp2) {
            System.out.println(", " + tmp1 + "-" + counter.getIdCount(tmp1) +
                    ", " + tmp2 + "-" + counter.getIdCount(tmp2));
        } else {
            System.out.println(", " + tmp2 + "-" + counter.getIdCount(tmp2) +
                    ", " + tmp1 + "-" + counter.getIdCount(tmp1));
        }
    }
}
