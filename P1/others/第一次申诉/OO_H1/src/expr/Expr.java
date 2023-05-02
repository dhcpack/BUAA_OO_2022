package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Expr implements Factor {
    private HashMap<Integer, BigInteger> terms;

    public Expr() {
        this.terms = new HashMap<>();
    }

    public void setTerms(HashMap<Integer, BigInteger> terms) {
        this.terms = terms;
    }

    public HashMap<Integer, BigInteger> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int exp: terms.keySet()) {
            BigInteger coef = terms.get(exp);

            // 处理系数的符号， 避免 +- 出现
            if (Objects.equals(coef, BigInteger.ZERO)) {
                continue;
            } else if (coef.compareTo(BigInteger.ZERO) > 0) {
                res.append("+");
            }

            // 处理指数，避免 x**0 出现
            if (exp == 0) {
                res.append(coef);
            } else {
                if (Objects.equals(coef, BigInteger.valueOf(1))) {
                    res.append("x");
                } else if (Objects.equals(coef, BigInteger.valueOf(-1))) {
                    res.append("-x");
                } else {
                    res.append(coef);
                    res.append("*x");
                }
                // a * x 时不需 exp
                if (exp != 1) {
                    // "x*x".length() < "x**2".length()
                    if (exp != 2) {
                        res.append("**");
                        res.append(exp);
                    } else {
                        res.append("*x");
                    }
                }
            }
        }

        if (res.length() == 0) {
            return "0";
        }

        // 头部 "+" 优化掉
        if (res.charAt(0) == '+') {
            return res.substring(1, res.length());
        }

        return res.toString();
    }

    public void addTerm(Term term) {
        Iterator<Factor> it = term.getFactor().iterator();
        Factor factor = it.next();

        HashMap<Integer, BigInteger> tmp = new HashMap<>();
        for (int key: factor.getTerms().keySet()) {
            tmp.put(key, factor.getTerms().get(key));
        }

        BigInteger sign = BigInteger.valueOf(term.getSign());
        while (it.hasNext()) {
            factor = it.next();
            tmp = multi(tmp, factor.getTerms());
        }
        for (int key: tmp.keySet()) {
            tmp.replace(key, sign.multiply(tmp.get(key)));
        }

        for (int key: tmp.keySet()) {
            if (terms.containsKey(key)) {
                terms.replace(key, tmp.get(key).add(terms.get(key)));
            } else {
                terms.put(key, tmp.get(key));
            }
        }
    }

    private HashMap<Integer, BigInteger> multi(HashMap<Integer, BigInteger> op1,
                                                HashMap<Integer, BigInteger> op2) {
        // Pattern : a * x ** b
        HashMap<Integer, BigInteger> res = new HashMap<>();

        for (int a1: op1.keySet()) {
            BigInteger b1 = op1.get(a1);
            for (int a2: op2.keySet()) {
                BigInteger b2 = op2.get(a2);
                int exp = a1 + a2;
                if (res.containsKey(exp)) {
                    BigInteger coef = res.get(exp);
                    res.replace(exp, coef.add(b1.multiply(b2)));
                } else {
                    res.put(exp, b1.multiply(b2));
                }
            }
        }

        return res;
    }

    public HashMap<Integer, BigInteger> calcPow(int num) {
        HashMap<Integer, BigInteger> newTerms = new HashMap<>();
        if (Objects.equals(num, 0)) {
            newTerms.put(0, BigInteger.ONE);
            return newTerms;
        }

        for (int key: terms.keySet()) {
            newTerms.put(key, terms.get(key));
        }

        for (int i = 1; i < num; i++) {
            newTerms = multi(newTerms, terms);
        }

        return newTerms;
    }

}
