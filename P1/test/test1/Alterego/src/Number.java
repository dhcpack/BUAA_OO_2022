import java.math.BigInteger;
import java.util.HashMap;

public class Number implements Factor {
    private final HashMap<Integer, BigInteger> number = new HashMap<>();

    public Number(BigInteger coef) {
        number.put(0, coef);
    }

    public HashMap<Integer, BigInteger> getTerms() {
        return number;
    }

}
