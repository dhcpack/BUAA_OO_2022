import java.math.BigInteger;
import java.util.HashMap;

public class Power implements Factor {
    private final HashMap<Integer, BigInteger> power = new HashMap<>();

    public Power(Integer degree) {
        power.put(degree, new BigInteger("1"));
    }

    public HashMap<Integer, BigInteger> getTerms() {
        return power;
    }
}
