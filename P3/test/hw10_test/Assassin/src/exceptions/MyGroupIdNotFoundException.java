package exceptions;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Counter counter = new Counter();
    private int id;

    public MyGroupIdNotFoundException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + counter.getNum() + ", " + id + "-" + counter.getNum(this.id));
    }
}
