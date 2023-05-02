package sentence;

import mathexpression.MathExpre;

/**
 * The {@code Factor} is the base-class of every part of an expression.
 * 
 * <p> Since {@code PowerFunct} , {@code Const} , and {@code Expr} are types of factors 
 * (Variable factor, Constant factor, Expression factor, respectively), 
 * {@code Factor} is suitable to be the base-class.
 */
public interface Factor {

    /**
     * Turn the factor into a string with math convention
     * @return the string of this {@code factor} 
     */
    @Override public String toString();

    /**
     * Substitute {@code expression} target for the {@code factor} source in this factor.
     * 
     * <p>In the substitution, this {@code factor} is not changed. Substitution only take place
     * in the return value. 
     * 
     * <p>{@code Expression factor}  type are return, since it's a type of {@code factor} .
     * Returning
     * an {@code expression} may lead to disorder of type hierachy in sentence package.
     * @param source the substitution factor
     * @param target the substituted factor
     * @return the {@code expression factor} after the substitution
     */
    public Factor substitute(Variable var, Expre target);
    
    public MathExpre toMathExpre();

}
