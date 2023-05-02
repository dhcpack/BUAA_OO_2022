package expr;

import poly.PolyItem;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import static poly.Polynomial.addPoly;

public class Expr implements Factor {
    private int index = 1;
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public int getRepe() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        while (iter.hasNext()) {
            sb.append(iter.next().toString());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Polynomial toPolynomial() {
        Polynomial polynomial = new Polynomial();
        polynomial.addPolyItem(new PolyItem("", BigInteger.valueOf(0), 0));
        for (Term term : terms) {
            polynomial = addPoly(polynomial, term.toPolynomial());
        }
        //System.out.println("expr: " + this + " " + polynomial);
        return polynomial;
    }
}
