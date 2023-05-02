import java.math.BigInteger;
import java.util.HashSet;

public class Term {
    private final HashSet<Factor> factors;
    private final BigInteger sign;

    public Term(int sign) {
        this.factors = new HashSet<>();
        this.sign = new BigInteger(String.valueOf(sign));
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public HashSet<Factor> getFactor() {
        return factors;
    }

    public BigInteger getSign() {
        return sign;
    }
}
