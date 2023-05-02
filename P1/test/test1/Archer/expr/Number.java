package expr;

import poly.PolyItem;
import poly.Polynomial;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

    @Override
    public int getRepe() {
        return 1;
    }

    public String toString() {
        return this.num.toString();
    }

    @Override
    public Polynomial toPolynomial() {
        PolyItem polyItem = new PolyItem("", num, 0);
        Polynomial polynomial = new Polynomial();
        polynomial.getPolyItems().add(polyItem);
        //System.out.println("number: " + this + " " + polynomial);
        return polynomial;
    }
}
