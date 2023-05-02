import java.math.BigInteger;
import java.util.HashMap;

public class Expression {
    //Hashmap<Hashmap<因子,指数>,系数>
    private HashMap<HashMap<String, BigInteger>, BigInteger> expressions = new HashMap<>();

    /*
    表达式构造
     */
    public Expression(String in) {
        HashMap<String, BigInteger> key = new HashMap<>();
        if (in.matches("-?\\d+")) {
            BigInteger num = new BigInteger(in);
            key.put("x", BigInteger.ZERO);
            expressions.put(key, num);
        } else {
            if (in.startsWith("x^")) {
                key.put("x", new BigInteger(in.substring(2)));
                expressions.put(key, BigInteger.ONE);
            } else {
                key.put("x", BigInteger.ONE);
                expressions.put(key, BigInteger.ONE);
            }
        }


    }

    public Expression(Expression expression) {
        expressions = expression.getExpressions();
    }

    public HashMap<HashMap<String, BigInteger>, BigInteger> getExpressions() {
        return this.expressions;
    }

    /*
    多项式的加法
     */
    public void add(Expression exp) {
        for (HashMap<String, BigInteger> key : exp.getExpressions().keySet()) {
            BigInteger val = exp.getExpressions().get(key);
            if (expressions.containsKey(key)) {
                BigInteger value = val.add(expressions.get(key));
                if (value.equals(BigInteger.ZERO)) {
                    expressions.remove(key);
                } else {
                    expressions.put(key, value);
                }
            } else {
                expressions.put(key, val);
            }
        }
    }

    /*
    多项式的乘法
     */
    public void mul(Expression exp) {
        HashMap<HashMap<String, BigInteger>, BigInteger> re = new HashMap<>();
        for (HashMap<String, BigInteger> hash1 : expressions.keySet()) {
            BigInteger val1 = expressions.get(hash1);
            for (HashMap<String, BigInteger> hash2 : exp.getExpressions().keySet()) {
                HashMap<String, BigInteger> newKey = new HashMap<>(hash1);
                BigInteger value;
                BigInteger val2 = exp.getExpressions().get(hash2);
                value = val1.multiply(val2);
                for (String key1 : hash2.keySet()) {
                    newKey.merge(key1, hash2.get(key1), BigInteger::add);
                }
                re.merge(newKey, value, BigInteger::add);
            }
        }
        expressions = re;
    }

    /*
    多项式的指数次方
     */
    public void pow(Expression exp) {
        HashMap<String, BigInteger> key0 = new HashMap<>();
        key0.put("x", BigInteger.ZERO);
        BigInteger n = exp.getExpressions().get(key0);
        if (n.equals(BigInteger.ZERO)) {
            expressions.clear();
            expressions.put(key0, BigInteger.ONE);
        } else {
            Expression now = new Expression(this);
            for (BigInteger i = BigInteger.ONE; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
                this.mul(now);
            }
        }
    }

    /*
    对多项式取sin
     */
    public void sin() {
        HashMap<HashMap<String, BigInteger>, BigInteger> re = new HashMap<>();
        HashMap<String, BigInteger> newKey = new HashMap<>();
        for (HashMap<String, BigInteger> key : expressions.keySet()) {
            BigInteger value = expressions.get(key);
            BigInteger expo = key.get("x");
            if (expo.equals(BigInteger.ZERO)) {
                if (value.equals(BigInteger.ZERO)) {
                    re.put(key, value);
                } else {
                    newKey.put("sin(" + value + ")", BigInteger.ONE);
                    re.put(newKey, BigInteger.ONE);
                }
            } else if (expo.equals(BigInteger.ONE)) {
                newKey.put("sin(x)", BigInteger.ONE);
                re.put(newKey, BigInteger.ONE);
            } else {
                newKey.put("sin(x**" + expo + ")", BigInteger.ONE);
                re.put(newKey, BigInteger.ONE);
            }
        }
        expressions = re;
    }

    /*
    对多项式取cos
     */
    public void cos() {
        HashMap<HashMap<String, BigInteger>, BigInteger> re = new HashMap<>();
        HashMap<String, BigInteger> newKey = new HashMap<>();
        for (HashMap<String, BigInteger> key : expressions.keySet()) {
            BigInteger value = expressions.get(key);
            BigInteger expo = key.get("x");
            if (expo.equals(BigInteger.ZERO)) {
                if (value.equals(BigInteger.ZERO)) {
                    newKey.put("x", BigInteger.ZERO);
                    re.put(newKey, BigInteger.ONE);
                } else {
                    newKey.put("cos(" + value + ")", BigInteger.ONE);
                    re.put(newKey, BigInteger.ONE);
                }
            } else if (expo.equals(BigInteger.ONE)) {
                newKey.put("cos(x)", BigInteger.ONE);
                re.put(newKey, BigInteger.ONE);
            } else {
                newKey.put("cos(x**" + expo + ")", BigInteger.ONE);
                re.put(newKey, BigInteger.ONE);
            }
        }
        expressions = re;
    }
}
