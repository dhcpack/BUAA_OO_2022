package expr;

import polynomial.Poly;
import polynomial.Polynomial;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;
    private String sign = "+";

    public Number(BigInteger num) {
        this.num = num;
    }

    public Polynomial ergodic() {
        Polynomial polynomial = new Polynomial();
        polynomial.addPoly(new Poly(num,0,sign));
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
