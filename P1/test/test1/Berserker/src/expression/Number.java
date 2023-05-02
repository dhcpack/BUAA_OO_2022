package expression;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;

public class Number implements Factor
{
    private BigInteger num;

    public Number(String numString)
    {
        num = new BigInteger(numString);
    }

    @Override
    public String toString()
    {
        if (num.equals(BigInteger.ZERO))
        {
            return "";
        }
        else
        {
            return num.toString();
        }
    }

    /**
     * 将数字转换成多项式类
     * @return 返回一个多项式，数字就是系数，指数是 0
     */
    @Override
    public Polynomial toPolynomial()
    {
        return new Polynomial(new PolyTerm(num, BigInteger.ZERO));
    }
}
