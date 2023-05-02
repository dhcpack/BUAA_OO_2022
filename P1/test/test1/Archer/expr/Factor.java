package expr;

import poly.Polynomial;

public interface Factor {
    int getRepe();

    Polynomial toPolynomial();
}
