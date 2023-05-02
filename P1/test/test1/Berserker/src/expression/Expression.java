package expression;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class Expression implements Factor
{
    private ArrayList<Term> terms = new ArrayList<>();
    private BigInteger power = new BigInteger("1");

    public void addTerm(Term term)
    {
        this.terms.add(term);
    }

    public void setPower(BigInteger power)
    {
        this.power = power;
    }

    @Override
    public Polynomial toPolynomial()
    {
        Polynomial p = new Polynomial(new PolyTerm(BigInteger.ONE, BigInteger.ZERO));
        Polynomial factor = singleExpressionToPolynomial();
        int n = power.intValue();
        for (int i = 0; i < n; i++)
        {
            p = p.multiply(factor);
        }
        return p;
    }

    private Polynomial singleExpressionToPolynomial()
    {
        Polynomial p = new Polynomial();
        for (Term term : terms)
        {
            p.addTerms(term.toPolynomial());
        }

        return p;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder(singleExpressionToString());
        int n = power.intValue() - 1;
        for (int i = 0; i < n; i++)
        {
            s.append("*");
            s.append(singleExpressionToString());
        }

        return s.toString();
    }

    private String singleExpressionToString()
    {
        String s = "";
        int i = 0;
        for (Term term : terms)
        {
            String termString = term.toString();
            if (!termString.equals(""))
            {
                s += (i != 0 && termString.charAt(0) != '+'
                        && termString.charAt(0) != '-') ? "+" : "";
                s += termString;
            }
            i++;
        }
        return s.equals("") ? "" : "(" + s + ")";
    }
}
