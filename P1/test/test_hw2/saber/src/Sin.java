import java.math.BigInteger;

public class Sin implements Factor {
    private Factor factor;
    private BigInteger expo;

    public Sin(Factor factor, BigInteger expo) {
        this.factor = factor;
        this.expo = expo;
    }

    public BigInteger getExpo() {
        return this.expo;
    }

    public Factor getFactor() {
        return this.factor;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.factor.toString());
        sb.append(" sin");
        if (!this.expo.equals(BigInteger.ONE)) {
            sb.append(" ");
            sb.append(expo);
            sb.append(" ^");
        }
        return sb.toString();
    }
}
