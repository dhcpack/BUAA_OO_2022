package ast;

import poly.Polynomial;

import java.util.HashMap;
import java.util.Map;

public class CustomFunc implements Node
{
    private Node expr;
    private HashMap<Integer, Node> arguments = new HashMap<>();

    public CustomFunc(Node expr, HashMap<Integer, Node> arguments)
    {
        this.expr = expr;
        this.arguments = arguments;
    }

    public Polynomial toPoly(HashMap<Integer, Node> arguments)
    {
        // 在这里完成了替换
        return expr.toPoly(this.arguments);
    }

    @Override
    public String toString()
    {
        String s = "[" + expr.toString() + "](";
        for (Map.Entry<Integer, Node> entry : arguments.entrySet())
        {
            s += entry.getValue().toString() + ", ";
        }
        return s.substring(0, s.length() - 2) + ")";
    }
}
