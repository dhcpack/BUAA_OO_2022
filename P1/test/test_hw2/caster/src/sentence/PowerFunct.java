package sentence;

import java.math.BigInteger;
import java.util.HashMap;

import mathexpression.MathExpre;
import mathexpression.NormalTerm;
import mathexpression.TermElement;
import mathexpression.VarElement;

public class PowerFunct implements Function {
   
    private Variable var;

    private BigInteger index;

    public PowerFunct(String varName) {
        var = new Variable(varName);
        index = new BigInteger("0");
    }

    public PowerFunct(String varName, BigInteger index) {
        var = new Variable(varName);
        this.index = index;
    }
    
    public BigInteger getIndex() {
        return index;
    }

    @Override public Factor substitute(Variable subVar, Expre target) {
        if (var.equals(subVar)) {
            return new ExprFactor(target, index);
        }
        return this;
    }

    @Override public String toString() {
        return var + "**" + index;
    }

    @Override public MathExpre toMathExpre() {
        HashMap<TermElement, BigInteger> coefsTerm = new HashMap<>();
        coefsTerm.put(
            new VarElement(var.getVarName()), 
            new BigInteger("" + index)
        );
        HashMap<NormalTerm, BigInteger> coefsExpr = new HashMap<>();
        coefsExpr.put(
            new NormalTerm(coefsTerm),
            new BigInteger("1")
        );
        return new MathExpre(coefsExpr);
    }

}
