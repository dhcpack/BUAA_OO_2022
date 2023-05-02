package expr;

import java.math.BigInteger;
import java.util.HashMap;

public class Pow implements Factor {
    private final HashMap<Integer, BigInteger> pow;

    public Pow() {
        pow = new HashMap<>();
    }

    @Override
    public HashMap<Integer, BigInteger> getTerms() {
        return pow;
    }

    // 幂函数因子: x ** b
    public void addTerms(int num) {
        pow.put(num, BigInteger.ONE);
    }

}
