package sentence;

import java.util.ArrayList;

import mathexpression.MathExpre;

/**
 * {@code Term} class represents a {@code term} in a sentence. 
 * 
 * <p>Grammar definition: {@code Term -> [+|-]Factor{*Factor}} 
 * 
 * <p>Math definition: The product of {@code factor} and sign.
 * 
 * <p>The object shall always stays the same after initialized, and methods changing 
 * field in object will clone the object and return a new object, the old object 
 * could be discarded.
 */
public class Term {

    /** Factors whose product is part of the {@code term} represented by this object. */
    private final ArrayList<Factor> factors;

    /**
     * The sign to be added before the product of {@code factors} . 
     * 
     * <p>It has nothing to do with the sign of {@link Factor} object.
     * E.g. {@code positive} = {@code false} and {@code factors} = -1 means 
     * {@code - -1 = 1} in Math.
    */
    private final boolean positive;

    /** Construct an empty {@code term} with positive sign */
    public Term() {
        factors = new ArrayList<>();
        positive = true;
    }

    public Term(ArrayList<Factor> factors, boolean positive) {
        this.factors = new ArrayList<>(factors);
        this.positive = positive;
    }

    /**
     * Set the sign.
     * 
     * <p>Original sign will be replaced.
     * @param positive the new sign
     */
    Term setPositive(boolean positive) {
        return new Term(factors, positive);
    }

    /**
     * Add a {@code factor} into this {@code term} .
     * 
     * <p>In Math, this {@code term} will be multiplied by {@code factor} .
     * 
     * @param factor the {@code factor } to be added into this {@code term} 
     */
    public Term add(Factor factor) {
        ArrayList<Factor> newFactors = new ArrayList<>(factors);
        newFactors.add(factor);
        return new Term(newFactors, positive);
    }
    
    /**
     * Reverse the sign of {@code positive} in this {@code term} .
     */
    public Term negate() {
        return new Term(factors, !positive);
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public Term substitute(Variable var, Expre target) {
        ArrayList<Factor> newFactors = new ArrayList<>();
        for (Factor fac : factors) {
            newFactors.add(fac.substitute(var, target));
        }
        return new Term(newFactors, positive);
    }
    
    @Override public String toString() {
        if (factors.size() == 0) {
            return "";
        }
        
        String ans = "";
        if (!positive) {
            ans += "-";
        }
        ans += factors.get(0);
        for (int i = 1; i < factors.size(); ++i) {
            ans += "*";
            ans += factors.get(i);
        }
        return ans;
    }

    public MathExpre toMathExpre() {
        MathExpre ans = new MathExpre();
        if (factors.size() == 0) {
            return ans;
        }
        ans = ans.add(factors.get(0).toMathExpre());
        for (int i = 1; i < factors.size(); ++i) {
            ans = ans.mult(factors.get(i).toMathExpre());
        }
        if (!positive) {
            ans = ans.negate();
        }
        return ans;
    }
}
