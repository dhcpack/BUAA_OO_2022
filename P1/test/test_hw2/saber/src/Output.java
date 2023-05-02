import java.math.BigInteger;
import java.util.HashMap;

public class Output {
    public static String string(Expression expression) {
        StringBuilder re = new StringBuilder();
        for (HashMap<String, BigInteger> hash : expression.getExpressions().keySet()) {
            BigInteger coe = expression.getExpressions().get(hash);
            if (coe.compareTo(BigInteger.ZERO) > 0) {  //系数为正
                re = pos(coe, hash, re);
            } else if (coe.compareTo(BigInteger.ZERO) < 0) { //系数为负
                re = neg(coe, hash, re);
            }
        }
        return finalHandle(re);
    }

    public static StringBuilder pos(BigInteger coe,
                                    HashMap<String, BigInteger> hash, StringBuilder re) {
        StringBuilder ext = new StringBuilder();
        ext.append("+");
        if (coe.equals(BigInteger.ONE)) {
            for (String key : hash.keySet()) {
                BigInteger value = hash.get(key);
                if (value.equals(BigInteger.ONE)) {
                    ext.append(key).append("*");
                } else if (!value.equals(BigInteger.ZERO)) {
                    ext.append(key).append("**").append(value).append("*");
                }
            }
            if (ext.length() == 1) {
                ext.append("1");
            }
        } else {
            ext.append(coe).append("*");
            for (String key : hash.keySet()) {
                BigInteger value = hash.get(key);
                if (value.equals(BigInteger.ONE)) {
                    ext.append(key).append("*");
                } else if (!value.equals(BigInteger.ZERO)) {
                    ext.append(key).append("**").append(value).append("*");
                }
            }
        }
        return re.append(ext);
    }

    public static StringBuilder neg(BigInteger coe,
                                    HashMap<String, BigInteger> hash, StringBuilder re) {
        StringBuilder ext = new StringBuilder();
        if (coe.equals(BigInteger.valueOf(-1))) {
            ext.append("-");
            for (String key : hash.keySet()) {
                BigInteger value = hash.get(key);
                if (value.equals(BigInteger.ONE)) {
                    ext.append(key).append("*");
                } else if (!value.equals(BigInteger.ZERO)) {
                    ext.append(key).append("**").append(value).append("*");
                }
            }
            if (ext.length() == 1) {
                ext.append("1");
            }
        } else {
            ext.append(coe).append("*");
            for (String key : hash.keySet()) {
                BigInteger value = hash.get(key);
                if (value.equals(BigInteger.ONE)) {
                    ext.append(key).append("*");
                } else if (!value.equals(BigInteger.ZERO)) {
                    ext.append(key).append("**").append(value).append("*");
                }
            }
        }
        return re.append(ext);
    }

    public static String finalHandle(StringBuilder re) {
        String re1 = re.toString();
        re1 = re1.replaceAll("\\*\\+", "+");
        re1 = re1.replaceAll("\\*-", "-");
        re1 = re1.replaceAll("\\*$", "");
        if (re1.startsWith("+")) {
            re1 = re1.substring(1);
        }
        if (re1.equals("")) {
            re1 = "0";
        }
        return re1;
    }
}
