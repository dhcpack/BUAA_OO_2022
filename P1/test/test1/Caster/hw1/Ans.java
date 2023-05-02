import java.math.BigInteger;
import java.util.ArrayList;

public class Ans {
    private final ArrayList<String> signs = new ArrayList<>();
    private final ArrayList<BigInteger> bigIntegers = new ArrayList<>();
    private final ArrayList<Integer> indexs = new ArrayList<>();

    public void addAns(String sign,BigInteger bigInteger,int index) {
        signs.add(sign);
        bigIntegers.add(bigInteger);
        indexs.add(index);
    }

    public void out() {
        boolean flag = true;
        int op = -1;
        for (int i = 0;i < signs.size();i++) {
            if (signs.get(i).equals("+")) {
                flag = false;
                op = i;
                c(i);
                break;
            }
        }
        for (int i = 0;i < signs.size();i++) {
            if (i != op) {
                flag = false;
                System.out.print(signs.get(i));
                c(i);
            }
        }
        if (flag) {
            System.out.print("0");
        }
    }

    public void c(int i) {
        BigInteger one = new BigInteger("1");
        if (indexs.get(i) == 0) {
            System.out.print(bigIntegers.get(i));
        } else if (indexs.get(i) == 1) {
            if (bigIntegers.get(i).compareTo(one) != 0) {
                System.out.print(bigIntegers.get(i));
                System.out.print("*x");
            } else {
                System.out.print("x");
            }
        } else if (indexs.get(i) == 2) {
            if (bigIntegers.get(i).compareTo(one) != 0) {
                System.out.print(bigIntegers.get(i));
                System.out.print("*x*x");
            } else {
                System.out.print("x*x");
            }
        } else {
            if (bigIntegers.get(i).compareTo(one) != 0) {
                System.out.print(bigIntegers.get(i));
                System.out.print("*x**");
            } else {
                System.out.print("x**");
            }
            System.out.print(indexs.get(i));
        }
    }
}
