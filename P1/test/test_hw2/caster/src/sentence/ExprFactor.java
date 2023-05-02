package sentence;

import java.math.BigInteger;

import mathexpression.MathExpre;

/**
 * {@code ExprFactor} class represents an {@code expression factor} .
 * 
 * <p>Grammar definition: {@code (Expre)[Index]} 
 * 
 * <p>Math definition: As convention.
 * 
 */
public class ExprFactor implements Factor {

    /** The {@code expression} between parentheses. */
    private Expre expression;

    /**
     * The index.
     * 
     * <p>If the origin sentence not include index, this field shall be initialized to 1.
     */
    private BigInteger index;

    /**
     * Construct an empty {@code expression factor} with index 1.
     */
    public ExprFactor() {
        expression = null;
        index = new BigInteger("1");
    }

    /**
     * Construct an {@code expression factor} with an {@link Expre} reference and
     * index.
     * 
     * @param expression the {@code expression} between parentheses
     * @param index the index
     */
    public ExprFactor(Expre expression, BigInteger index) {
        this.expression = expression;
        this.index = index;
    }

    @Override public String toString() {
        return "(" + expression + ")**" + index;
    }

    @Override public Factor substitute(Variable var, Expre target) {
        Expre newExpression = expression.substitute(var, target);
        return new ExprFactor(newExpression, index);
    }

    @Override public MathExpre toMathExpre() {
        if (index.equals(BigInteger.ZERO)) {
            return new ConstFactor(new BigInteger("1")).toMathExpre();
        }
        MathExpre base = expression.toMathExpre();
        MathExpre ans = base;
        for (BigInteger cur = new BigInteger("1");
            cur.compareTo(index) < 0;
            cur = cur.add(BigInteger.ONE)
        ) {
            ans = ans.mult(base);
        }
        return ans;
    }
}
