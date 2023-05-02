package sentence;

import java.math.BigInteger;
import java.util.HashMap;

import mathexpression.MathExpre;
import mathexpression.NormalTerm;
import mathexpression.TermElement;
import mathexpression.TriFunctElement;

public class TriFunct implements Function {
    public static enum Type {
        SIN,
        COS,
    }

    public TriFunct(Type type, BigInteger index, Factor inside) {
        this.type = type;
        this.index = index;
        this.inside = inside;
    }

    public TriFunct(parser.Lexer.Type type, BigInteger index, Factor inside) {
        this.index = index;
        this.inside = inside;
        if (type == parser.Lexer.Type.COS) {
            this.type = Type.COS;
        } else {
            this.type = Type.SIN;
        }
    }

    private Type type;

    private BigInteger index;

    private Factor inside;

    public Type getType() {
        return type;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Factor getInside() {
        return inside;
    }

    @Override public Factor substitute(Variable var, Expre target) {
        Factor newInside = inside.substitute(var, target);
        return new TriFunct(type, index, newInside);
    }

    @Override public String toString() {
        String ans = "";
        if (type == Type.SIN) {
            ans = "sin";
        } else {
            ans = "cos";
        }
        ans += "(" + inside + ")**" + index;
        return ans;
    }

    @Override public MathExpre toMathExpre() {
        boolean insideNeg = false;
        MathExpre insideMath = inside.toMathExpre();
        if (insideMath.toSimpleString().equals("0")) {
            if (type == Type.SIN) {
                return new ConstFactor(new BigInteger("0")).toMathExpre();
            } else {
                return new ConstFactor(new BigInteger("1")).toMathExpre();
            }
        }

        if (insideMath.negate().toSimpleString().length() 
            < insideMath.toSimpleString().length()
        ) {
            insideMath = insideMath.negate();
            insideNeg = true;
        }

        boolean oddFunct = (
            type == Type.SIN && index.mod(new BigInteger("2")).equals(BigInteger.ONE)
        );
        HashMap<TermElement, BigInteger> coefsTerm = new HashMap<>();
        coefsTerm.put(
            new TriFunctElement(type, insideMath), 
            index
        );
        HashMap<NormalTerm, BigInteger> coefsExpr = new HashMap<>();
        coefsExpr.put(
            new NormalTerm(coefsTerm),
            new BigInteger("1")
        );
        if (insideNeg && oddFunct) {
            return new MathExpre(coefsExpr).negate();
        }
        return new MathExpre(coefsExpr);
    }
}
