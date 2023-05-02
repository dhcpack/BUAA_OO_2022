import com.oocourse.spec3.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static Count count = new Count();
    
    public MyEqualRelationException(int id1, int id2) {
        count.addSum();
        if (id1 == id2) {
            count.add(id1);
        } else {
            count.add(id1);
            count.add(id2);
        }
        this.id1 = Math.min(id1, id2);
        this.id2 = (this.id1 == id1) ? id2 : id1;
    }
    
    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d\n", count.getTotalCount(), id1,
                count.getSpecCount(id1), id2, count.getSpecCount(id2));
    }
}
