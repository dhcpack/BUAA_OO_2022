package exceptions;

import com.oocourse.spec2.exceptions.EqualRelationException;
import main.Calculator;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private final int type = 3;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updatePerCal(type, this.id1);
        if (id1 != id2) {
            Calculator.getInstance().updatePerCal(type, this.id2);
        }
    }

    @Override
    public void print() {
        int timesExc = Calculator.getInstance().getExcCal(type);
        int timesPer1 = Calculator.getInstance().getPerCal(type, id1);
        int timesPer2 = Calculator.getInstance().getPerCal(type, id2);
        System.out.println("er-" + timesExc + ", " +
                id1 + "-" + timesPer1 + ", " + id2 + "-" + timesPer2);
    }
}
