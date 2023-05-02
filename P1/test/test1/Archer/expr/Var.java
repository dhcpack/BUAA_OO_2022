package expr;

import poly.PolyItem;
import poly.Polynomial;

import java.math.BigInteger;

public class Var implements Factor {
    private int index = 1;
    private String name;

    public Var() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getRepe() {
        return 1;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        if (index == 1) {
            return name;
        } else {
            return name + "^" + index;
        }
    }

    @Override
    public Polynomial toPolynomial() {
        PolyItem polyItem =  new PolyItem(name, BigInteger.valueOf(1), index);
        Polynomial polynomial = new Polynomial();
        polynomial.getPolyItems().add(polyItem);
        //System.out.println("var: " + this + " " + polynomial);
        return polynomial;
    }
}
