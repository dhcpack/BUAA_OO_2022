package simplifiedexpr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SimplifiedExpr {
    private final HashMap<SimplifiedTerm, BigInteger> simplifiedTerms;

    public SimplifiedExpr() {
        simplifiedTerms = new HashMap<>();
    }

    public SimplifiedExpr(SimplifiedExpr simplifiedExpr) {
        simplifiedTerms = new HashMap<>();
        for (SimplifiedTerm simplifiedTerm : simplifiedExpr.simplifiedTerms.keySet()) {
            simplifiedTerms.put(new SimplifiedTerm(simplifiedTerm), new
                    BigInteger(String.valueOf(simplifiedExpr.simplifiedTerms.get(simplifiedTerm))));
        }
    }

    public HashMap<SimplifiedTerm, BigInteger> getSimplifiedTerms() {
        return simplifiedTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedExpr that = (SimplifiedExpr) o;
        return Objects.equals(simplifiedTerms, that.simplifiedTerms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simplifiedTerms);
    }

    public SimplifiedExpr add(SimplifiedExpr augend) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm : this.simplifiedTerms.keySet()) {
            result.simplifiedTerms.put(simplifiedTerm, this.simplifiedTerms.get(simplifiedTerm));
        }
        for (SimplifiedTerm simplifiedTerm : augend.simplifiedTerms.keySet()) {
            if (result.simplifiedTerms.containsKey(simplifiedTerm)) {
                result.simplifiedTerms.put(simplifiedTerm, result.simplifiedTerms.
                        get(simplifiedTerm).add(augend.simplifiedTerms.get(simplifiedTerm)));
            } else {
                result.simplifiedTerms.put(simplifiedTerm, augend.simplifiedTerms.
                        get(simplifiedTerm));
            }
        }
        return result;
    }

    public SimplifiedExpr multiply(SimplifiedExpr multiplicand) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm1 : this.simplifiedTerms.keySet()) {
            for (SimplifiedTerm simplifiedTerm2 : multiplicand.simplifiedTerms.keySet()) {
                BigInteger bigInteger = this.simplifiedTerms.get(simplifiedTerm1).
                        multiply(multiplicand.simplifiedTerms.get(simplifiedTerm2));
                SimplifiedTerm simplifiedTerm = simplifiedTerm1.multiply(simplifiedTerm2);
                SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
                simplifiedExpr.simplifiedTerms.put(simplifiedTerm, bigInteger);
                result = result.add(simplifiedExpr);
            }
        }
        return result;
    }

    public SimplifiedExpr negate() {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm : simplifiedTerms.keySet()) {
            result.simplifiedTerms.put(simplifiedTerm,
                    simplifiedTerms.get(simplifiedTerm).negate());
        }
        return result;
    }

    public SimplifiedExpr pow(int n) {
        SimplifiedExpr result = new SimplifiedExpr();
        SimplifiedTerm simplifiedTerm = new SimplifiedTerm();
        result.simplifiedTerms.put(simplifiedTerm, new BigInteger("1"));
        for (int i = 0; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public void func(SimplifiedTerm simplifiedTerm1, SimplifiedTerm simplifiedTerm2,
                     SimplifiedTerm tempTerm1, SimplifiedTerm tempTerm2) {
        if (simplifiedTerms.containsKey(tempTerm1)) {
            simplifiedTerms.put(tempTerm1, simplifiedTerms.
                    get(simplifiedTerm1).add(simplifiedTerms.get(tempTerm1)));
        } else {
            simplifiedTerms.put(tempTerm1, simplifiedTerms.get(simplifiedTerm1));
        }
        simplifiedTerms.put(simplifiedTerm2, simplifiedTerms.get(simplifiedTerm2).
                subtract(simplifiedTerms.get(simplifiedTerm1)));
        simplifiedTerms.remove(simplifiedTerm1);
    }

    public boolean merge() {
        boolean flag = false;
        OUT:
        for (SimplifiedTerm simplifiedTerm1 : simplifiedTerms.keySet()) {
            SimplifiedTerm tempTerm1 = new SimplifiedTerm(simplifiedTerm1);
            if (simplifiedTerms.get(tempTerm1).equals(BigInteger.ZERO)) {
                continue;
            }
            ArrayList<SimplifiedBase> simplifiedBases = tempTerm1.func();
            for (SimplifiedBase simplifiedBase1 : simplifiedBases) {
                SimplifiedBase simplifiedBase2 = new SimplifiedFunc("cos",
                        new SimplifiedExpr(((SimplifiedFunc) simplifiedBase1).getSimplifiedExpr()));
                if (tempTerm1.getSimplifiedBases().get(simplifiedBase1) == 2) {
                    tempTerm1.getSimplifiedBases().remove(simplifiedBase1);
                } else {
                    tempTerm1.getSimplifiedBases().put(simplifiedBase1,
                            tempTerm1.getSimplifiedBases().get(simplifiedBase1) - 2);
                }
                for (SimplifiedTerm simplifiedTerm2 : simplifiedTerms.keySet()) {
                    SimplifiedTerm tempTerm2 = new SimplifiedTerm(simplifiedTerm2);
                    if (simplifiedTerm1 == simplifiedTerm2 ||
                            simplifiedTerms.get(tempTerm2).equals(BigInteger.ZERO)) {
                        continue;
                    }
                    if (tempTerm2.getSimplifiedBases().containsKey(simplifiedBase2) &&
                            tempTerm2.getSimplifiedBases().get(simplifiedBase2) >= 2) {
                        if (tempTerm2.getSimplifiedBases().get(simplifiedBase2) == 2) {
                            tempTerm2.getSimplifiedBases().remove(simplifiedBase2);
                        } else {
                            tempTerm2.getSimplifiedBases().put(simplifiedBase2,
                                    tempTerm2.getSimplifiedBases().get(simplifiedBase2) - 2);
                        }
                        BigInteger bigInteger1 = simplifiedTerms.get(simplifiedTerm1);
                        BigInteger bigInteger2 = simplifiedTerms.get(simplifiedTerm2);
                        if (tempTerm1.equals(tempTerm2) &&
                                bigInteger1.multiply(bigInteger2).compareTo(BigInteger.ZERO) > 0) {
                            if (bigInteger1.compareTo(BigInteger.ZERO) > 0 &&
                                    bigInteger2.compareTo(BigInteger.ZERO) > 0) {
                                if (bigInteger1.compareTo(bigInteger2) <= 0) {
                                    func(simplifiedTerm1, simplifiedTerm2, tempTerm1, tempTerm2);
                                } else {
                                    func(simplifiedTerm2, simplifiedTerm1, tempTerm2, tempTerm1);
                                }
                            } else if (bigInteger1.compareTo(BigInteger.ZERO) < 0 &&
                                    bigInteger2.compareTo(BigInteger.ZERO) < 0) {
                                if (bigInteger1.compareTo(bigInteger2) <= 0) {
                                    func(simplifiedTerm2, simplifiedTerm1, tempTerm2, tempTerm1);
                                } else {
                                    func(simplifiedTerm1, simplifiedTerm2, tempTerm1, tempTerm2);
                                }
                            }
                            flag = true;
                            break OUT;
                        }
                    }
                }
            }
        }
        return flag;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SimplifiedTerm simplifiedTerm : simplifiedTerms.keySet()) {
            if (!simplifiedTerms.get(simplifiedTerm).equals(BigInteger.ZERO)) {
                stringBuilder.append("+");
                stringBuilder.append(simplifiedTerms.get(simplifiedTerm));
                if (simplifiedTerm.getSimplifiedBases().size() != 0) {
                    stringBuilder.append("*");
                    stringBuilder.append(simplifiedTerm);
                }
            }
        }
        String ret = String.valueOf(stringBuilder);
        ret = ret.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
        ret = ret.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
        ret = ret.replaceAll("\\+1\\*", "+");
        ret = ret.replaceAll("-1\\*", "-");
        ret = ret.replaceAll("\\+-", "-");
        if (ret.equals("")) {
            ret = "0";
        }
        if (ret.charAt(0) == '+') {
            ret = ret.substring(1);
        }
        return ret;
    }
}
