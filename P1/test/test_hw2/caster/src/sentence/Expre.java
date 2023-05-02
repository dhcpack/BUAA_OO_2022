package sentence;

import java.util.ArrayList;

import mathexpression.MathExpre;

/**
 * {@code Expre} class represents an {@code Expre} in a sentence. 
 * 
 * <p>Grammar definition:   {@code Expre -> [+|-]Term{(+|-)Term}} 
 * 
 * <p>Math definition: The sumation of {@code terms}
 * 
 * <p>The object shall always stays the same after initialized, and methods changing 
 * field in object will clone the object and return a new object, the old object 
 * could be discarded.
 */
public class Expre {

    /** Terms whose sumation is this {@code expression} object. */
    private final ArrayList<Term> terms;

    /** Construct an empty {@code Expre} with no term. */
    public Expre() {
        terms = new ArrayList<>();
    }

    public Expre(ArrayList<Term> terms) {
        this.terms = new ArrayList<>(terms);
    }

    /**
     * Add a {@code term} into this {@code Expre} .
     * 
     * <p>The sign is included in class {@link Term}.
     * 
     * @param term the {@code term} to add
     */
    public Expre add(Term term) {
        ArrayList<Term> newTerms = new ArrayList<>(terms);
        newTerms.add(term);
        return new Expre(newTerms);
    }

    /**
     * Add a {@code term} into this {@code Expre} and set sign.
     * 
     * <p>The sign in {@code term} is replaced by {@code positive} .
     * 
     * @param positive the final sign of the {@code term} 
     * @param term the {@code term} to add
     */
    public Expre add(boolean positive, Term term) {
        ArrayList<Term> newTerms = new ArrayList<>(terms);
        Term newTerm = term.setPositive(positive);
        newTerms.add(newTerm);
        return new Expre(newTerms);
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    /**
     * Substitute {@code expression} target for the {@code factor} source in
     * this {@code expression} .
     * 
     * <p>In the substitution, this {@code expression} is not changed. Substitution only take place
     * in the return value. 
     * @param source the substitution factor
     * @param target the substituted expression
     * @return the {@code expression} after the substitution
     */
    public Expre substitute(Variable var, Expre target) {
        Expre ans = new Expre();
        for (Term t : terms) {
            ans = ans.add(t.substitute(var, target));
        }
        return ans;
    }

    @Override public String toString() {
        if (terms.size() == 0) {
            return "";
        }
        String ans = terms.get(0).toString();
        for (int i = 1; i < terms.size(); ++i) {
            ans += "+" + terms.get(i);
        }
        return ans;
    }

    public MathExpre toMathExpre() {
        MathExpre ans = new MathExpre();
        for (Term term : terms) {
            ans = ans.add(term.toMathExpre());
        }
        return ans;
    }
}
