package expression;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;

public class PowerFunc implements Factor
{
    private BigInteger power;

    public PowerFunc(String powerString)
    {
        power = new BigInteger(powerString);
    }

    @Override
    public String toString()
    {
        if (power.equals(BigInteger.ZERO))
        {
            return "1";
        }
        else if (power.equals(BigInteger.ONE))
        {
            return "x";
        }
        else if (power.equals(BigInteger.valueOf(2)))
        {
            return "x*x";
        }
        else
        {
            return "x**" + power.toString();
        }
    }

    /**
     * 将幂函数转成一个多项式类
     * @return 系数是 1，指数就是幂函数的指数
     */
    @Override
    public Polynomial toPolynomial()
    {
        return new Polynomial(new PolyTerm(BigInteger.ONE, power));
    }
}
