package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private final HashSet<Factor> factors;
    private final BigInteger sign;

    public Term(int sign) {
        this.factors = new HashSet<>();
        this.sign = new BigInteger(String.valueOf(sign));
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public HashMap<Integer, BigInteger> calc() {
        HashMap<Integer, BigInteger> res = new HashMap<>();  // (degree, coef)->指数，系数
        Iterator<Factor> iterator = factors.iterator();
        Factor factor = iterator.next();
        if (factor instanceof Number) {
            Number number = (Number) factor;
            res.put(0, number.getNum());
        } else if (factor instanceof Power) {
            Power power = (Power) factor;
            res.put(power.getDegree(), new BigInteger("1"));
        } else if (factor instanceof Expr) {
            Expr expr = (Expr) factor;
            res.putAll(expr.getTerms());
        }
        while (iterator.hasNext()) {
            factor = iterator.next();
            if (factor instanceof Number) {
                Number number = (Number) factor;
                for (Integer key : res.keySet()) {
                    res.replace(key, res.get(key).multiply(number.getNum()));
                }
            } else if (factor instanceof Power) {
                Power power = (Power) factor;
                HashMap<Integer, BigInteger> resCopy = new HashMap<>();
                for (Integer key : res.keySet()) {
                    resCopy.put(key * power.getDegree(), res.get(key));
                }
                res = resCopy;
            } else if (factor instanceof Expr) {
                Expr expr = (Expr) factor;
                HashMap<Integer, BigInteger> resCopy = new HashMap<>();
                HashMap<Integer, BigInteger> terms = expr.getTerms();
                for (Integer degree1 : res.keySet()) {
                    BigInteger coef1 = res.get(degree1);
                    for (Integer degree2 : terms.keySet()) {
                        BigInteger coef2 = terms.get(degree2);
                        resCopy.put(degree1 + degree2, coef1.multiply(coef2));
                    }
                }
                res = resCopy;
            }
        }
        HashMap<Integer, BigInteger> resCopy  = new HashMap<>();
        for (Integer degree : res.keySet()) {
            resCopy.put(degree, res.get(degree).multiply(sign));
        }
        return resCopy;
    }
}
