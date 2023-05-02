package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.HashMap;

public class Cos implements Node
{
    private Node factor;

    public Cos(Node factor)
    {
        this.factor = factor;
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();
        PolyTerm polyTerm = new PolyTerm(BigInteger.ONE, BigInteger.ZERO);
        HashMap<Polynomial, BigInteger> cos = new HashMap<>();
        cos.put(factor.toPoly(arguments), BigInteger.ONE);
        polyTerm.setCos(cos);
        polynomial.addPolyTerm(polyTerm);

        return polynomial;
    }

    @Override
    public String toString()
    {
        return "cos(" + factor.toString() + ")";
    }
}
