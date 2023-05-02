import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

public class Calculator {

    public HashMap<Integer, BigInteger> mutiply(HashMap<Integer, BigInteger> terms1,
                                                HashMap<Integer, BigInteger> terms2) {
        HashMap<Integer, BigInteger> res = new HashMap<>();  // (degree, coef)->指数，系数
        for (Integer degree1 : terms1.keySet()) {
            BigInteger coef1 = terms1.get(degree1);
            for (Integer degree2 : terms2.keySet()) {
                BigInteger coef2 = terms2.get(degree2);
                if (res.containsKey(degree1 + degree2)) {
                    res.put(degree1 + degree2,
                            res.get(degree1 + degree2).add(coef1.multiply(coef2)));
                } else {
                    res.put(degree1 + degree2, coef1.multiply(coef2));
                }
            }
        }
        return res;
    }

    public HashMap<Integer, BigInteger> power(Factor expr, int times) {
        HashMap<Integer, BigInteger> res = new HashMap<>();
        HashMap<Integer, BigInteger> terms = expr.getTerms();
        if (times == 0) {
            res.put(0, new BigInteger("1"));
            return res;
        }
        for (Integer degree : terms.keySet()) {
            res.put(degree, terms.get(degree));
        }
        for (int i = 1; i < times; i++) {
            res = mutiply(res, terms);
        }
        return res;
    }

    public HashMap<Integer, BigInteger> calcTerms(Term term) {
        HashMap<Integer, BigInteger> res = new HashMap<>();
        Iterator<Factor> iterator = term.getFactor().iterator();
        Factor factor = iterator.next();
        for (Integer degree : factor.getTerms().keySet()) {
            res.put(degree, factor.getTerms().get(degree));
        }
        while (iterator.hasNext()) {
            factor = iterator.next();
            res = mutiply(res, factor.getTerms());
        }
        BigInteger sign = term.getSign();
        HashMap<Integer, BigInteger> resCopy = new HashMap<>();
        for (Integer degree : res.keySet()) {
            resCopy.put(degree, res.get(degree).multiply(sign));
        }
        return resCopy;
    }

}


