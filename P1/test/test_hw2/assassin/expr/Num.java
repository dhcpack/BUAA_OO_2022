package expr;

import simplifiedexpr.SimplifiedExpr;
import simplifiedexpr.SimplifiedTerm;
import java.math.BigInteger;

public class Num implements Base {
    private final BigInteger num;

    public Num(BigInteger num) {
        this.num = num;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        SimplifiedTerm simplifiedTerm = new SimplifiedTerm();
        simplifiedExpr.getSimplifiedTerms().
                put(simplifiedTerm, new BigInteger(String.valueOf(num)));
        return simplifiedExpr;
    }
}
