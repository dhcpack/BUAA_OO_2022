package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import main.Calculator;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int type = 5;
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updateGroupCal(type, id);
    }

    @Override
    public void print() {
        int timesExc = Calculator.getInstance().getExcCal(type);
        int timesGroup = Calculator.getInstance().getGroupCal(type, id);
        System.out.println("egi-" + timesExc + ", " + id + "-" + timesGroup);
    }
}
