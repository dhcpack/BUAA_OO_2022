import java.math.BigInteger;
import java.util.HashMap;

public class Expr implements Factor {
    private HashMap<Integer, BigInteger> terms = new HashMap<>();  // (degree, coef)->指数，系数

    public Expr() {
    }

    public Expr(HashMap<Integer, BigInteger> terms) {
        this.terms = terms;
    }

    public HashMap<Integer, BigInteger> getTerms() {
        return terms;
    }

    public void mergeTerms(Term term) {
        Calculator calculator = new Calculator();
        HashMap<Integer, BigInteger> toMerge = calculator.calcTerms(term);
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
            StringBuilder attached = new StringBuilder();
            if (!coef.equals(zero)) {
                attached.append(coef);

                if (degree != 0) {
                    if (coef.equals(new BigInteger("1")) || coef.equals(new BigInteger("-1"))) {
                        attached.deleteCharAt(attached.length() - 1);
                    } else {
                        attached.append("*");
                    }
                    attached.append("x");
                    if (degree != 1 && degree != 2) {
                        attached.append("**").append(degree);
                    } else if (degree == 2) {
                        attached.append("*x");
                    }
                }

                if (coef.compareTo(zero) > 0) {
                    attached.insert(0, "+");
                    stringBuilder.insert(0, attached);
                } else {
                    stringBuilder.append(attached);
                }
            }
        }
        if (stringBuilder.length() == 0) {
            return "0";
        } else {
            String res = stringBuilder.toString();
            if (res.charAt(0) == '+') {
                return res.substring(1);
            }
            return res;
        }


    }
}
