package expr;

import polynomial.Poly;
import polynomial.Polynomial;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Expr implements Factor {
    private final HashSet<Term> terms;
    private Term first;
    private String sign = "+";
    private int index = 1;

    public Expr() {
        this.terms = new HashSet<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void setFirst(Term first) {
        this.first = first;
    }

    public Polynomial ergodic() {
        Iterator<Term> iter = terms.iterator();
        Term term = iter.next();
        term.setSign(sign);
        Polynomial polynomial = term.ergodic();
        while (iter.hasNext()) {
            polynomial.addPolynomial(iter.next().ergodic());
        }
        Polynomial polynomial1 = new Polynomial();
        polynomial1.copy(polynomial);
        for (int i = 1;i < index;i++) {
            polynomial.multPolynomial(polynomial1);
        }
        if (index == 0) {
            Polynomial polynomial2 = new Polynomial();
            BigInteger bigInteger = new BigInteger("1");
            Poly p = new Poly(bigInteger,0,"+");
            polynomial2.addPoly(p);
            return polynomial2;
        }
        return polynomial;
    }

    public void setSign(String sign) {
        if (sign.equals("-")) {
            if (this.sign.equals("+")) {
                this.sign = "-";
            } else {
                this.sign = "+";
            }
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
