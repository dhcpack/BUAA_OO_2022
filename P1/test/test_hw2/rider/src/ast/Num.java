package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.HashMap;

public class Num implements Node
{
    private BigInteger value;

    /**
     * 这里不存在深浅复制的问题
     * @param numString 表示数字的字符串，用其他类型容易范围缩小
     */
    public Num(String numString)
    {
        value = new BigInteger(numString);
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();
        PolyTerm polyTerm = new PolyTerm(value, BigInteger.ZERO);
        polynomial.addPolyTerm(polyTerm);
        return polynomial;
    }

    @Override
    public String toString()
    {
        if (value.equals(BigInteger.ZERO))
        {
            return "";
        }
        else
        {
            return value.toString();
        }
    }
}
