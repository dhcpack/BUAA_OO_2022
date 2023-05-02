package exceptions;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter = new Counter();
    private int id;

    public MyPersonIdNotFoundException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + counter.getNum() + ", " + id + "-" + counter.getNum(this.id));
    }
}
