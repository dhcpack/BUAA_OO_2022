package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import main.Calculator;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int type = 1;
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updatePerCal(type, id);
    }

    @Override
    public void print() {
        int timesExc = Calculator.getInstance().getExcCal(type);
        int timesPer = Calculator.getInstance().getPerCal(type, id);
        System.out.println("epi-" + timesExc + ", " + id + "-" + timesPer);
    }
}
