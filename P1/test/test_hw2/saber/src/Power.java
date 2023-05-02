import java.math.BigInteger;

public class Power implements Factor {
    private BigInteger expo;

    public Power(BigInteger expo) {
        this.expo = expo;
    }

    public BigInteger getExpo() {
        return expo;
    }

    public String toString() {
        String re;
        if (getExpo().equals(BigInteger.ONE)) {
            re = "x";
        } else {
            re = "x^" + getExpo();
        }
        return re;
    }
}