package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class Expr implements Factor {
    private HashMap<Integer, BigInteger> terms = new HashMap<>();  // (degree, coef)->指数，系数

    public HashMap<Integer, BigInteger> getTerms() {
        return terms;
    }

    public void mergeTerms(Term term) {
        HashMap<Integer, BigInteger> toMerge = term.calc();
        for (Integer degree : toMerge.keySet()) {
            if (terms.containsKey(degree)) {
                terms.replace(degree, terms.get(degree).add(toMerge.get(degree)));
            } else {
                terms.put(degree, toMerge.get(degree));
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer degree : terms.keySet()) {
            BigInteger coef = terms.get(degree);
            BigInteger zero = new BigInteger("0");
            if (!coef.equals(zero)) {
                if (coef.compareTo(zero) > 0) {
                    stringBuilder.append("+");
                }
                stringBuilder.append(coef);
                if (degree != 0) {
                    stringBuilder.append("*x");
                    if (degree != 1) {
                        stringBuilder.append("**").append(degree);
                    }
                }
            }
        }
        if (stringBuilder.length() == 0) {
            return "0";
        } else {
            return stringBuilder.toString();
        }
    }
}
