package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.HashMap;

public class Power implements Node
{
    private Node coefficient;
    private long exponent;

    public Power(Node coe, String expo)
    {
        this.coefficient = coe;
        this.exponent = Long.parseLong(expo);
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();
        polynomial.addPolyTerm(new PolyTerm(BigInteger.ONE, BigInteger.ZERO));
        Polynomial factor = coefficient.toPoly(arguments);

        for (long i = 0; i < exponent; i++)
        {
            polynomial = polynomial.multi(factor);
        }
        return polynomial;
    }

    @Override
    public String toString()
    {
        if (exponent == 0)
        {
            return "1";
        }
        else if (exponent == 1)
        {
            return coefficient.toString();
        }
        else
        {
            return coefficient.toString() + "**" + exponent;
        }
    }
}
