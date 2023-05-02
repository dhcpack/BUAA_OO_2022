package expr;

import java.util.HashSet;

public class Term {
    private final HashSet<Factor> factors;
    private final int sign;

    public Term(int sign) {
        this.factors = new HashSet<>();
        this.sign = sign;
    }

    public HashSet<Factor> getFactor() {
        return factors;
    }

    public int getSign() {
        return sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

}
