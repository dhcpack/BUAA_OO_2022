package expr;

import poly.PolyItem;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import static poly.Polynomial.multiplyPoly;

public class Term {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        int index = factor.getRepe();
        while (index >= 1) {
            this.factors.add(factor);
            index--;
        }
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        if (iter.hasNext()) {
            sb.append(iter.next().toString());
            while (iter.hasNext()) {
                sb.append("*");
                sb.append(iter.next().toString());
            }
        }
        return sb.toString();
    }

    public Polynomial toPolynomial() {
        Polynomial polynomial = new Polynomial();
        polynomial.addPolyItem(new PolyItem("", BigInteger.valueOf(1), 0));
        for (Factor factor : factors) {
            polynomial = multiplyPoly(polynomial, factor.toPolynomial());
        }
        //System.out.println("term: " + this + " " + polynomial);
        return polynomial;
    }
}
