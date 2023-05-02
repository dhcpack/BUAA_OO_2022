package poly;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PolyTerm
{
    private BigInteger coefficient;
    private BigInteger exponent;
    // 这个里面的 key 是 sin 或者 cos 里面的表达式，value 是 sin 或者 cos 的指数
    private HashMap<Polynomial, BigInteger> sin = new HashMap<>();
    private HashMap<Polynomial, BigInteger> cos = new HashMap<>();

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

    public void setSin(HashMap<Polynomial, BigInteger> sin)
    {
        this.sin = sin;
    }

    public void setCos(HashMap<Polynomial, BigInteger> cos)
    {
        this.cos = cos;
    }

    public PolyTerm clone()
    {
        BigInteger copyCoe = new BigInteger(coefficient.toString());
        BigInteger copyExpo = new BigInteger(exponent.toString());

        HashMap<Polynomial, BigInteger> copySin = new HashMap<>();
        HashMap<Polynomial, BigInteger> copyCos = new HashMap<>();
        for (Map.Entry<Polynomial, BigInteger> entry : sin.entrySet())
        {
            copySin.put(entry.getKey().clone(), new BigInteger(entry.getValue().toString()));
        }
        for (Map.Entry<Polynomial, BigInteger> entry : cos.entrySet())
        {
            copyCos.put(entry.getKey().clone(), new BigInteger(entry.getValue().toString()));
        }

        PolyTerm copy = new PolyTerm(copyCoe, copyExpo);
        copy.cos = copyCos;
        copy.sin = copySin;

        return copy;
    }

    public PolyTerm multi(PolyTerm other)
    {
        PolyTerm polyTerm = clone();
        polyTerm.coefficient = this.coefficient.multiply(other.coefficient);
        polyTerm.exponent = this.exponent.add(other.exponent);

        for (Polynomial p : other.sin.keySet())
        {
            if (this.sin.containsKey(p))
            {
                polyTerm.sin.put(p, sin.get(p).add(other.sin.get(p)));
            }
            else
            {
                polyTerm.sin.put(p, other.sin.get(p));
            }
        }

        for (Polynomial p : other.cos.keySet())
        {
            if (this.cos.containsKey(p))
            {
                polyTerm.cos.put(p, cos.get(p).add(other.cos.get(p)));
            }
            else
            {
                polyTerm.cos.put(p, other.cos.get(p));
            }
        }
        return polyTerm;
    }

    /**
     * 如果指数，sin，cos 均相同，则认为是同一个类型
     * @param other 另一个比较
     * @return 是否相等
     */
    public boolean isSameType(PolyTerm other)
    {
        if (other == null)
        {
            return false;
        }
        else
        {
            return exponent.equals(other.exponent) &&
                    sin.equals(other.sin) && cos.equals(other.cos);
        }
    }

    /**
     * 当值为 0 的时候，会返回 ""，交由 Polynomial 的 toString 处理
     * @return
     */
    @Override
    public String toString()
    {
        String s = null;
        // 如果系数为 0, 那么就返回 “”
        if (coefficient.equals(BigInteger.ZERO))
        {
            return "";
        }
        else
        {
            //在系数为 1 的时候，不考虑输出系数
            if (coefficient.equals(BigInteger.ONE))
            {
                s = "";
            }
            //在系数为 -1 的时候，只考虑输出负号
            else if (coefficient.equals(new BigInteger("-1")))
            {
                s = "-";
            }
            else
            {
                s = coefficient.toString() + "*";
            }
            // 当指数为 0 的时候，啥都不输出
            if (!exponent.equals(BigInteger.ZERO))
            {
                // 当指数为 1 的时候，输出 x
                if (exponent.equals(BigInteger.ONE))
                {
                    s += "x*";
                }
                else
                {
                    s += "x**" + exponent.toString() + "*";
                }
            }

            s += triangleToString(sin, "sin");
            s += triangleToString(cos, "cos");
            // 当 s 为空串的时候，只可能是系数为 1，指数为 0，三角没有的情况
            return (s.equals("")) ? "1" :
                    (s.equals("-")) ? "-1" : s.substring(0, s.length() - 1);
        }
    }

    private String triangleToString(HashMap<Polynomial, BigInteger> tria, String name)
    {
        String s = "";
        for (Map.Entry<Polynomial, BigInteger> entry : tria.entrySet())
        {
            // 当三角函数指数为 0 的时候, 三角函数为 1， 故不输出
            if (!entry.getValue().equals(BigInteger.ZERO))
            {
                // 先输出三角函数本身
                s += name + "(" + entry.getKey().toString() + ")";
                // 当三角函数的指数为 1 的时候，不输出指数
                if (entry.getValue().equals(BigInteger.ONE))
                {
                    s += "*";
                }
                else
                {
                    s += "**" + entry.getValue().toString() + "*";
                }
            }
        }
        return s;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        PolyTerm polyTerm = (PolyTerm) o;
        return Objects.equals(coefficient, polyTerm.coefficient) &&
                Objects.equals(exponent, polyTerm.exponent) &&
                Objects.equals(sin, polyTerm.sin) &&
                Objects.equals(cos, polyTerm.cos);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(coefficient, exponent, sin, cos);
    }
}
