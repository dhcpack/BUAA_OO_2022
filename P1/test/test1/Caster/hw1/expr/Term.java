package expr;

import polynomial.Poly;
import polynomial.Polynomial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private final HashSet<Factor> factors;
    private Factor first;
    private String sign = "+";

    public Term() {
        factors = new HashSet<>();
    }

    public void setFirst(Factor first) {
        this.first = first;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Polynomial ergodic() {
        Iterator<Factor> iter = factors.iterator();
        Polynomial polynomial = new Polynomial();
        polynomial.addPolynomial(first.ergodic());
        if (sign.equals("-")) {
            ArrayList<Poly> po = polynomial.getPolies();
            for (Poly poly : po) {
                poly.reverseSign();
            }
        }
        while (iter.hasNext()) {
            polynomial.multPolynomial(iter.next().ergodic());
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
}
