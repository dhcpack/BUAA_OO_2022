package poly;

import java.math.BigInteger;

public class PolyItem {
    private String name;
    private BigInteger coe;
    private int index;

    public BigInteger getCoe() {
        return coe;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolyItem(String name, BigInteger coe, int index) {
        this.name = name;
        this.coe = coe;
        this.index = index;
    }

    public PolyItem(String name, BigInteger coe) {
        this.name = name;
        this.coe = coe;
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return coe.toString();
        } else if (index == 1) {
            if (coe.compareTo(BigInteger.valueOf(1)) == 0) {
                return name;
            } else {
                return coe + "*" + name;
            }
        } else {
            if (coe.compareTo(BigInteger.valueOf(1)) == 0) {
                return name + "**" + index;
            } else {
                return coe + "*" + name + "**" + index;
            }
        }
    }
}
