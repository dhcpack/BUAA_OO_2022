package expr;

import java.math.BigInteger;
import java.util.HashMap;

public class Number implements Factor {
    private final HashMap<Integer, BigInteger> num = new HashMap<>();

    public Number(BigInteger constant) {
        num.put(0, constant);
    }

    @Override
    public HashMap<Integer, BigInteger> getTerms() {
        return num;
    }
}
