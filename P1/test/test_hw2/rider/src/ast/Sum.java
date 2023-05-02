package ast;

import poly.Polynomial;

import java.util.HashMap;

public class Sum implements Node
{
    private long begin;
    private long end;
    private Node factor;

    public Sum(String begin, String end, Node factor)
    {
        this.begin = Long.parseLong(begin);
        this.end = Long.parseLong(end);
        this.factor = factor;
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();

        for (long i = begin; i <= end; i++)
        {
            HashMap<Integer, Node> iterArg = new HashMap<>();
            iterArg.put(4, new Num(String.valueOf(i)));
            Polynomial iterPoly = factor.toPoly(iterArg);
            polynomial = polynomial.add(iterPoly);
        }

        return polynomial;
    }

    @Override
    public String toString()
    {
        return "sum(" + begin + ", " + end + ", " + factor.toString() + ")";
    }
}
