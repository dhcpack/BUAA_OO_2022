package mathexpression;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * {@code NormalTerm} class represents a normal term in an math expression. 
 * The normal form is:
 * 
 * <p>{@code k x^p sin^a1(Factor) ... sin^an(Factor) cos^b1(Factor) ... cos^bm(Factor)} 
 * 
 * <p>A {@code normal term} has several factors, each with one {@code term element} ,
 * such as {@code TriFunctElement}  and {@code VarElement} , and with one index.
 * Therefore, a normal term is similar to the x^p in one polynomial.
 * 
 * <p>Example: followings are normal terms:
 * 
 * <p>{@code sin(x**2)**2} , {@code cos(1)} , {@code x**4*cos(sin(x))} , {@code x**0} 
 * 
 * <p>and followings are not normal terms: {@code 4*x**0} , {@code 4*sin(x)} 
 * 
 * <p>Note that the factor container is not allowed to be empty, at least it has one 
 * term {@code x**0} . {@code x**0} will be inserted when constructing.
 */
public class NormalTerm {

    private final HashMap<TermElement, BigInteger> factors;

    /** Construct a {@code normal term} with 0*x**0 */
    public NormalTerm() {
        factors = new HashMap<>();
        factors.put(new VarElement("x"), new BigInteger("0"));
    }

    /** 
     * Construct a {@code normal Term} with given factor maps, and add x**0 if neccessary.
     * @param factors Initial relationship between {@code term element} and its' index
     */
    public NormalTerm(HashMap<TermElement, BigInteger> factors) {
        this.factors = new HashMap<>(factors);
        this.factors.merge(new VarElement("x"), new BigInteger("0"), BigInteger::add);
    }

    /**
     * The result of this term multiplied by another {@code normal term}.
     * 
     * <p>This {@code normal term} would not be changed.
     * @param term the {@code normala term} to multiply by
     * @return the answer
     */
    NormalTerm mult(NormalTerm term) {
        NormalTerm ans = new NormalTerm(factors);
        for (TermElement element : term.factors.keySet()) {
            ans.factors.merge(element, term.factors.get(element), BigInteger::add);
        }
        return ans;
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        NormalTerm other = (NormalTerm)o;
        return factors.equals(other.factors);
    }

    @Override public int hashCode() {
        return factors.hashCode();
    }

    @Override public String toString() {
        StringBuilder ans = new StringBuilder("");
        for (TermElement factor : factors.keySet()) {
            ans.append("*");
            ans.append(factor.toString());
            ans.append("**");
            ans.append(factors.get(factor));
        }
        return ans.toString();
    }

    public String toSimpleString(boolean conservative) {
        String ans = "";
        for (TermElement factor : factors.keySet()) {
            String base = factor.toSimpleString(conservative);
            if (base.length() != 0) {
                if (factors.get(factor).equals(BigInteger.ZERO)) {
                    continue;
                } else if (!factors.get(factor).equals(BigInteger.ONE)) {
                    if (ans.length() != 0) {
                        ans += "*";
                    }
                    if (base.equals("x") && factors.get(factor).equals(new BigInteger("2"))
                         && !conservative
                    ) {
                        ans += "x*x";
                    } else {
                        ans += base + "**" + factors.get(factor);
                    }
                } else {
                    if (ans.length() != 0) {
                        ans += "*";
                    }
                    ans += base;
                }
            }
        }
        return ans.toString();
    }
}
