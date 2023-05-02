package expr;

import java.math.BigInteger;
import java.util.HashMap;

public interface Factor {
    HashMap<Integer, BigInteger> getTerms();
}
