package expr;

import polynomial.Poly;
import polynomial.Polynomial;

import java.math.BigInteger;

public class PowerFunc implements Factor {
    private final int index;
    private String sign = "+";

    public PowerFunc(int index) {
        this.index = index;
    }

    public Polynomial ergodic() {
        Polynomial polynomial = new Polynomial();
        BigInteger bigInteger = new BigInteger("1");
        polynomial.addPoly(new Poly(bigInteger,index,sign));
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
