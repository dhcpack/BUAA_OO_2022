package exceptions;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import main.Calculator;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int type = 0;
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updatePerCal(type, id);
    }

    @Override
    public void print() {
        int timesExc = Calculator.getInstance().getExcCal(type);
        int timesPer = Calculator.getInstance().getPerCal(type, id);
        System.out.println("pinf-" + timesExc + ", " + id + "-" + timesPer);
    }
}
