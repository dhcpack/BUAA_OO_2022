package expression;

import poly.Polynomial;

public interface Factor
{
    String toString();

    Polynomial toPolynomial();
}
