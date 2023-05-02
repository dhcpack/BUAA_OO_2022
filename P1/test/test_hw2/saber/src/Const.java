import java.math.BigInteger;

public class Const implements Factor {
    private BigInteger num;

    public Const(BigInteger s) {
        this.num = new BigInteger(String.valueOf(s));
    }

    public BigInteger getNum() {
        return num;
    }

    public String toString() {
        return this.num.toString();
    }
}