package mathexpression;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;

public class MathExpre {
   
    private final HashMap<NormalTerm, BigInteger> coefs;

    public MathExpre() {
        coefs = new HashMap<>();
    }

    /**
     * Construct a new {@code math expression} with a copy of the {@code HashMap} coefs.
     * @param coefs the {@code HashMap} to clone
     */
    public MathExpre(HashMap<NormalTerm, BigInteger> coefs) {
        this.coefs = new HashMap<>(coefs);
    }

    public MathExpre negate() {
        HashMap<NormalTerm, BigInteger> newCoefs = new HashMap<>();
        for (NormalTerm term : coefs.keySet()) {
            newCoefs.put(term, coefs.get(term).negate());
        }
        return new MathExpre(newCoefs);
    }

    public HashMap<NormalTerm, BigInteger> getCoefs() {
        return coefs;
    }

    public MathExpre add(MathExpre expre) {
        MathExpre ans = this;
        for (NormalTerm term : expre.getTermSet()) {
            ans = ans.addTerm(term, expre.getCoefs().get(term));
        }
        return ans;
    }

    public MathExpre mult(MathExpre expre) {
        MathExpre ans = new MathExpre();
        for (NormalTerm term : expre.getTermSet()) {
            ans = ans.add(this.multTerm(term, expre.getCoefs().get(term)));
        }
        return ans;
    }

    private MathExpre addTerm(NormalTerm term, BigInteger coef) {
        MathExpre ans = new MathExpre(coefs);
        ans.coefs.merge(term, coef, BigInteger::add);
        return ans;
    }

    private MathExpre multTerm(NormalTerm term, BigInteger coef) {
        MathExpre ans = new MathExpre();
        for (NormalTerm curTerm : this.getTermSet()) {
            ans = ans.addTerm(curTerm.mult(term), coef.multiply(coefs.get(curTerm)));
        }
        return ans;
    }

    private Set<NormalTerm> getTermSet() {
        return coefs.keySet();
    }

    boolean isEmpty() {
        return coefs.size() == 0;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        MathExpre other = (MathExpre)o;
        return coefs.equals(other.coefs);
    }

    @Override public int hashCode() {
        return coefs.hashCode();
    }

    @Override public String toString() {
        StringBuilder ans = new StringBuilder("");
        for (NormalTerm term : coefs.keySet()) {
            if (ans.length() > 0) {
                ans.append("+");
            }
            ans.append(coefs.get(term));
            ans.append(term);
        }
        return ans.toString();
    }

    private String factorInTermToSimpleString(NormalTerm term, boolean sign, boolean conservative) {
        String ans = "";
        boolean positive = coefs.get(term).compareTo(BigInteger.ZERO) > 0;
        String coefPart = coefs.get(term).abs().toString();
        String fixedPart = term.toSimpleString(conservative);
        if (coefs.get(term).equals(BigInteger.ZERO) || fixedPart.equals("0")) {
            return "";
        }

        if (positive && sign) {
            ans += "+";
        } else if (!positive) {
            ans += "-";
        }

        if (fixedPart.length() == 0) {
            ans += coefPart;
        } else {
            if (coefPart.equals("1")) {
                ans += fixedPart;
            } else {
                ans += coefPart + "*" + fixedPart;
            }
        }
        return ans;
    }

    public String toSimpleString() {
        return toSimpleString(false);
    }

    public String toSimpleString(boolean conservative) {
        String ans = "";
        NormalTerm firstPrintTerm = null;
        for (NormalTerm term : coefs.keySet()) {
            if (coefs.get(term).compareTo(BigInteger.ZERO) > 0) {
                firstPrintTerm = term;
                ans += factorInTermToSimpleString(term, false, conservative);
                break;
            }
        }
        for (NormalTerm term : coefs.keySet()) {
            if (term.equals(firstPrintTerm)) {
                continue;
            }
            ans += factorInTermToSimpleString(term, ans.length() != 0, conservative);
        }
        if (ans.length() == 0) {
            return "0";
        }
        return ans;
    }
}
