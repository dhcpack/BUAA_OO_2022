package sentence;

import java.math.BigInteger;
import java.util.HashMap;

import mathexpression.MathExpre;
import mathexpression.NormalTerm;
import mathexpression.TermElement;

/**
 * {@code Constant} class represents a {@code Constant Factor} .
 * 
 * <p>Grammar definition: {@code ConstFactor -> SignedInt} 
 * 
 * <p>Math definition: A number
 */
public class ConstFactor implements Factor {

    /** The value of this constant. */
    private BigInteger value;

    /** Construct a {@code constant factor} with value 0 */
    public ConstFactor() {
        value = new BigInteger("0");
    }

    /**
     * Construct a {@code constant factor} with given value
     * @param value
     */
    public ConstFactor(BigInteger value) {
        this.value = value;
    }

    public ConstFactor addOne() {
        return new ConstFactor(value.add(BigInteger.ONE));
    }

    public BigInteger getValue() {
        return value;
    }

    @Override public String toString() {
        return value.toString();
    }

    @Override public Factor substitute(Variable var, Expre target) {
        return this;
    }

    @Override public MathExpre toMathExpre() {
        HashMap<TermElement, BigInteger> coefsTerm = new HashMap<>();
        HashMap<NormalTerm, BigInteger> coefsExpr = new HashMap<>();
        coefsExpr.put(
            new NormalTerm(coefsTerm), 
            value
        );
        return new MathExpre(coefsExpr);
    }

}
