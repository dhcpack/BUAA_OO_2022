package ast;

import poly.Polynomial;

import java.util.HashMap;

public interface Node
{
    public Polynomial toPoly(HashMap<Integer, Node> arguments);
}
