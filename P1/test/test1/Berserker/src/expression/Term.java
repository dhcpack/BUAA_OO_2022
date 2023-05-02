package expression;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term
{
    private ArrayList<Factor> factors = new ArrayList<>();
    private int option; // 正负性

    public Term(int option)
    {
        this.option = option;
    }

    public void addFactor(Factor factor)
    {
        factors.add(factor);
    }

    public Polynomial toPolynomial()
    {
        Polynomial p = new Polynomial(new PolyTerm(option == 1 ?
                BigInteger.ONE : BigInteger.valueOf(-1),
                BigInteger.ZERO));
        for (Factor factor : factors)
        {
            p = p.multiply(factor.toPolynomial());
        }

        return p;
    }

    @Override
    public String toString()
    {
        Boolean haveOne = false;
        // 这里用于处理item的正负号问题
        String s = "";
        s += option == 1 ? "" : "-";

        for (Factor factor : factors)
        {
            String factorString = factor.toString();
            // 有常数因子0，整个项为0
            if (factorString.equals(""))
            {
                return "";
            }
            // 如果是 1 的话，是不需要加的
            if (factorString.equals("1"))
            {
                haveOne = true;
                continue;
            }
            // 生成item
            s += factorString + "*";
        }

        if (s.equals("") || s.equals("-") || s.equals("+"))
        {
            if (haveOne)
            {
                return s + "1";
            }
            else
            {
                return "";
            }
        }
        // 去掉最后的 “*”
        else
        {
            return s.substring(0, s.length() - 1);
        }
    }
}
