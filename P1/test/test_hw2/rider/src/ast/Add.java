package ast;

import poly.Polynomial;

import java.util.ArrayList;
import java.util.HashMap;

public class Add implements Node
{
    private ArrayList<Node> children = new ArrayList<>();

    public void addTerm(Node term)
    {
        this.children.add(term);
    }

    @Override
    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        Polynomial polynomial = new Polynomial();

        for (Node child : children)
        {
            polynomial = polynomial.add(child.toPoly(arguments));
        }
        return polynomial;
    }

    @Override
    public String toString()
    {
        String s = "";
        int i = 0;
        for (Node child : children)
        {
            String termString = child.toString();
            if (!termString.equals(""))
            {
                s += (i != 0 && termString.charAt(0) != '+'
                        && termString.charAt(0) != '-') ? "+" : "";
                s += termString;
            }
            i++;
        }
        return s.equals("") ? "0" : "(" + s + ")";
    }
}
