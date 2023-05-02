import java.math.BigInteger;

public class Term {
    private BigInteger factor;
    private int index;

    public Term() {
        this.factor = BigInteger.valueOf(1);
        this.index = 0;
    }

    public BigInteger getFactor() {
        return factor;
    }

    public int getIndex() {
        return index;
    }

    public void setFactor(BigInteger factor) {
        this.factor = factor;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
