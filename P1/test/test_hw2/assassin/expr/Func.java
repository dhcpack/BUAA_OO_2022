package expr;

import simplifiedexpr.SimplifiedExpr;
import simplifiedexpr.SimplifiedFunc;
import simplifiedexpr.SimplifiedTerm;

import java.math.BigInteger;

public class Func implements Base {
    private final String funcName;
    private final Expr expr;

    public Func(String funcName, Expr expr) {
        this.funcName = funcName;
        this.expr = expr;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        SimplifiedTerm simplifiedTerm = new SimplifiedTerm();
        SimplifiedExpr simplifiedExpr2 = expr.simplify();
        SimplifiedTerm simplifiedTerm2 = new SimplifiedTerm();
        simplifiedTerm.getSimplifiedBases().put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
        simplifiedExpr.getSimplifiedTerms().put(simplifiedTerm, new BigInteger("1"));
        if (funcName.equals("sin")) {
            if (simplifiedExpr2.getSimplifiedTerms().containsKey(simplifiedTerm2)) {
                if (simplifiedExpr2.getSimplifiedTerms().get(simplifiedTerm2).
                        compareTo(BigInteger.ZERO) < 0) {
                    simplifiedExpr.getSimplifiedTerms().clear();
                    simplifiedTerm.getSimplifiedBases().clear();
                    simplifiedExpr2.getSimplifiedTerms().put(simplifiedTerm2,
                            simplifiedExpr2.getSimplifiedTerms().get(simplifiedTerm2).negate());
                    simplifiedTerm.getSimplifiedBases().
                            put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
                    simplifiedExpr.getSimplifiedTerms().put(simplifiedTerm, new BigInteger("-1"));
                } else if (simplifiedExpr2.getSimplifiedTerms().
                        get(simplifiedTerm2).compareTo(BigInteger.ZERO) == 0) {
                    simplifiedExpr.getSimplifiedTerms().clear();
                    simplifiedTerm.getSimplifiedBases().clear();
                    simplifiedExpr.getSimplifiedTerms().put(simplifiedTerm, BigInteger.ZERO);
                }
            }
        } else {
            if (simplifiedExpr2.getSimplifiedTerms().containsKey(simplifiedTerm2)) {
                if (simplifiedExpr2.getSimplifiedTerms().
                        get(simplifiedTerm2).compareTo(BigInteger.ZERO) < 0) {
                    simplifiedExpr.getSimplifiedTerms().clear();
                    simplifiedTerm.getSimplifiedBases().clear();
                    simplifiedExpr2.getSimplifiedTerms().put(simplifiedTerm2,
                            simplifiedExpr2.getSimplifiedTerms().get(simplifiedTerm2).negate());
                    simplifiedTerm.getSimplifiedBases().
                            put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
                    simplifiedExpr.getSimplifiedTerms().put(simplifiedTerm, new BigInteger("1"));
                } else if (simplifiedExpr2.getSimplifiedTerms().
                        get(simplifiedTerm2).compareTo(BigInteger.ZERO) == 0) {
                    simplifiedExpr.getSimplifiedTerms().clear();
                    simplifiedTerm.getSimplifiedBases().clear();
                    simplifiedExpr.getSimplifiedTerms().put(simplifiedTerm, BigInteger.ONE);
                }
            }
        }
        return simplifiedExpr;
    }
}
