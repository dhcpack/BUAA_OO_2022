import java.math.BigInteger;
import java.util.ArrayList;

public class Result {
    private ArrayList<BigInteger> coef;
    private int firstPrint;

    public Result() {
        coef = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            coef.add(BigInteger.ZERO);
        }
        firstPrint = -1;
    }

    public void coefset(int index, BigInteger val) {
        coef.set(index, val);
    }

    public Result multiply(Result other) {
        Result ret = new Result();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i + j < 9) {
                    BigInteger now = ret.coef.get(i + j);
                    ret.coef.set(i + j, now.add(this.coef.get(i).multiply(other.coef.get(j))));
                }
            }
        }
        return ret;
    }

    public Result plus(Result other) {
        Result ret = new Result();
        for (int i = 0; i < 9; i++) {
            ret.coef.set(i, this.coef.get(i).add(other.coef.get(i)));
        }
        return ret;
    }

    public Result minus(Result other) {
        Result ret = new Result();
        for (int i = 0; i < 9; i++) {
            ret.coef.set(i, this.coef.get(i).subtract(other.coef.get(i)));
        }
        return ret;
    }

    public Result getpow(long times) {
        Result ret = new Result();
        ret.coef.set(0, BigInteger.ONE);
        for (long i = 1; i <= times; i++) {
            ret = ret.multiply(this);
        }
        return ret;
    }

    public Result toNeg() {
        Result ret = new Result();
        for (int i = 0; i < 9; i++) {
            ret.coef.set(i, this.coef.get(i).negate());
        }
        return ret;
    }

    public boolean printOne(boolean printed) {
        boolean ret = printed;
        if (coef.get(1).equals(BigInteger.ONE)) {
            if (printed) {
                System.out.print("+");
            }
            System.out.print("x");
            ret = true;
        } else if (coef.get(1).equals(BigInteger.valueOf(-1))) {
            System.out.print("-x");
            ret = true;
        } else if (!coef.get(1).equals(BigInteger.ZERO)) {
            if (printed && (coef.get(1).signum() > 0)) {
                System.out.print("+");
            }
            System.out.print(coef.get(1) + "*x");
            ret = true;
        }
        return ret;
    }

    public boolean printZero(boolean printed) {
        boolean ret = printed;
        if (!coef.get(0).equals(BigInteger.ZERO)) {
            if (printed && (coef.get(0).signum() > 0)) {
                System.out.print("+");
            }
            System.out.print(coef.get(0));
            ret = true;
        }
        return ret;
    }

    public boolean checkFirst() {
        for (int i = 8; i >= 2; i--) {
            if (coef.get(i).signum() > 0) {
                firstPrint = i;
                if (coef.get(i).equals(BigInteger.ONE)) {
                    if (i == 2) {
                        System.out.printf("x*x");
                    } else {
                        System.out.printf("x**%d", i);
                    }
                } else {
                    if (i == 2) {
                        System.out.print(coef.get(i) + "*x*x");
                    } else {
                        System.out.print(coef.get(i) + "*x**" + i);
                    }
                }
                break;
            }
        }
        if (firstPrint == -1 && (coef.get(1).signum() > 0)) {
            firstPrint = 1;
            printOne(false);
        } else if (firstPrint == -1 && (coef.get(0).signum() > 0)) {
            firstPrint = 0;
            printZero(false);
        }
        return (firstPrint != -1);  //had first non-sign output
    }

    public void print() {
        boolean printed = false;
        printed = checkFirst();
        for (int i = 8; i >= 2; i--) {
            if (firstPrint == i) {
                continue;
            } else if (coef.get(i).equals(BigInteger.ZERO)) {
                continue;
            } else if (coef.get(i).equals(BigInteger.ONE)) {
                if (printed) {
                    System.out.print("+");
                } else {
                    printed = true;
                }
                if (i == 2) {
                    System.out.printf("x*x");
                } else {
                    System.out.printf("x**%d", i);
                }
            } else if (coef.get(i).equals(BigInteger.valueOf(-1))) {
                if (i == 2) {
                    System.out.printf("-x*x");
                } else {
                    System.out.printf("-x**%d", i);
                }
                printed = true;
            } else {
                if (printed && (coef.get(i).signum() > 0)) {
                    System.out.print("+");
                } else if (!printed) {
                    printed = true;
                }
                if (i == 2) {
                    System.out.print(coef.get(i) + "*x*x");
                } else {
                    System.out.print(coef.get(i) + "*x**" + i);
                }
            }
        }
        if (firstPrint != 1) {
            printed = printOne(printed);
        }
        if (firstPrint != 0) {
            printed = printZero(printed);
        }
        if (!printed) {
            System.out.print("0");
        }
        System.out.print("\n");
    }
}
