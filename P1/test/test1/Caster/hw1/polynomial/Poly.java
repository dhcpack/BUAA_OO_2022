package polynomial;

import java.math.BigInteger;
import java.util.Objects;

public class Poly implements Comparable<Poly> {
    private final BigInteger coefficient;
    private String sign;
    private final int index;

    public Poly(BigInteger coefficient, int index, String sign) {
        this.coefficient = coefficient;
        this.index = index;
        this.sign = sign;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public int getIndex() {
        return index;
    }

    public void reverseSign() {
        if (Objects.equals(sign, "+")) {
            sign = "-";
        } else {
            sign = "+";
        }
    }

    public String getSign() {
        return sign;
    }

    @Override
    public int compareTo(Poly other) {
        if (this.getIndex() < other.getIndex()) {
            return -1;
        } else if (this.getIndex() > other.getIndex()) {
            return 1;
        }
        return 0;
    }
}
