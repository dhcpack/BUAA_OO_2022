import java.math.BigInteger;

public class TriFactor implements Comparable<TriFactor> {
    private BigInteger indexTri;
    private BigInteger coefTri;
    private String symbolTri;
    private BigInteger isIndex;

    public TriFactor(BigInteger index, BigInteger coef, String symbol, BigInteger isIndex) {
        this.indexTri = index;
        this.coefTri = coef;
        this.symbolTri = symbol;
        this.isIndex = isIndex;
    }

    public BigInteger getIndexTri() {
        return indexTri;
    }

    public BigInteger getCoefTri() {
        return coefTri;
    }

    public String getSymbolTri() {
        return symbolTri;
    }

    public BigInteger getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(BigInteger isIndex) {
        this.isIndex = isIndex;
    }

    public int compareTo(TriFactor other) {    //按指数将sin cos中的单项式从高到低排序
        if (this.indexTri.compareTo(other.indexTri) < 0) {
            return 1;
        } else if (this.indexTri.compareTo(other.indexTri) > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
