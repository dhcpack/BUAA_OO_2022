package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.HashMap;

public class Sin implements Node
{
    private Node factor;
    // 之所以不用 Power 节点处理，是因为这样太耗时间了

    public Sin(Node factor)
    {
        this.factor = factor;
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();
        PolyTerm polyTerm = new PolyTerm(BigInteger.ONE, BigInteger.ZERO);
        HashMap<Polynomial, BigInteger> sin = new HashMap<>();
        sin.put(factor.toPoly(arguments), BigInteger.ONE);
        polyTerm.setSin(sin);
        polynomial.addPolyTerm(polyTerm);

        return polynomial;
    }

    @Override
    public String toString()
    {
        return "sin(" + factor.toString() + ")";
    }
}
