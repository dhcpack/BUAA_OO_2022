package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.HashMap;

public class Variable implements Node
{
    private int pos;

    public Variable(int type)
    {
        this.pos = type;
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = null;
        // 这里生成一个系数为 1，指数为 1 的标准项，即 x
        if (pos == 0)
        {
            polynomial = new Polynomial();
            polynomial.addPolyTerm(new PolyTerm(BigInteger.ONE, BigInteger.ONE));
        }
        else
        {
            Node node = arguments.get(pos);
            polynomial = node.toPoly(arguments);
        }
        return polynomial;
    }

    public String toString()
    {
        if (pos == 0)
        {
            return "a";
        }
        else if (pos == 1)
        {
            return "x";
        }
        else if (pos == 2)
        {
            return "y";
        }
        else if (pos == 3)
        {
            return "z";
        }
        else
        {
            return "i";
        }
    }
}
