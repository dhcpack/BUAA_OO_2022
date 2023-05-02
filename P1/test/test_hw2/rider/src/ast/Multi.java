package ast;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Multi implements Node
{
    private ArrayList<Node> children = new ArrayList<>();

    public void addFactor(Node factor)
    {
        this.children.add(factor);
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();
        polynomial.addPolyTerm(new PolyTerm(BigInteger.ONE, BigInteger.ZERO));

        for (Node child : children)
        {
            polynomial = polynomial.multi(child.toPoly(arguments));
        }

        return polynomial;
    }

    @Override
    public String toString()
    {
        Boolean haveOne = false;
        // 这里用于处理item的正负号问题
        String s = "";

        for (Node child : children)
        {
            String factorString = child.toString();
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
