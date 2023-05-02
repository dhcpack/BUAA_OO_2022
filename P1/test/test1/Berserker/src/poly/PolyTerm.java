package poly;

import java.math.BigInteger;

public class PolyTerm
{
    private BigInteger coefficient;
    private BigInteger exponent;

    public PolyTerm(BigInteger coe, BigInteger expo)
    {
        coefficient = coe;
        exponent = expo;
    }

    public BigInteger getCoefficient()
    {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient)
    {
        this.coefficient = coefficient;
    }

    public BigInteger getExponent()
    {
        return exponent;
    }

    /**
     * 最基础的乘法原理
     */
    public PolyTerm multiply(PolyTerm other)
    {
        BigInteger coe = this.coefficient.multiply(other.coefficient);
        BigInteger expo = this.exponent.add(other.exponent);
        return new PolyTerm(coe, expo);
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        boolean isZero = false;
        boolean isOne = false;
        if (coefficient.equals(BigInteger.ZERO))
        {
            isZero = true;
        }
        else if (coefficient.equals(new BigInteger("-1")))
        {
            s.append("-");
        }
        else if (!coefficient.equals(BigInteger.ONE))
        {
            s.append(coefficient.toString() + "*");
        }

        if (exponent.equals(BigInteger.ZERO))
        {
            isOne = true;
        }
        else if (exponent.equals(BigInteger.ONE))
        {
            s.append("x");
        }
        else  if (exponent.equals(BigInteger.valueOf(2)))
        {
            s.append("x*x");
        }
        else
        {
            s.append("x**" + exponent.toString());
        }

        return isZero ? "" :
                isOne ? coefficient.toString() :
                        s.toString();
    }
}
