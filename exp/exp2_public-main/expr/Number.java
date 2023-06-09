package expr;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    @Override
    public Factor simplify() {
        return new Number(num);/* TODO */
    }
}
