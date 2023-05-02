import expr.Expr;
import polynomial.Poly;
import polynomial.Polynomial;
import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Polynomial polynomial = expr.ergodic();
        ArrayList<Poly> polies = polynomial.getPolies();
        Collections.sort(polies);
        String signnow = "+";
        BigInteger bigIntegernow = new BigInteger("0");
        int indexnow = 0;
        boolean start = true;
        BigInteger zero = new BigInteger("0");
        Ans ans = new Ans();

        for (Poly poly : polies) {
            if (start) {
                start = false;
                signnow = poly.getSign();
                bigIntegernow = bigIntegernow.add(poly.getCoefficient());
                indexnow = poly.getIndex();
                continue;
            }
            if (poly.getIndex() == indexnow) {
                if (Objects.equals(poly.getSign(), signnow)) {
                    bigIntegernow = bigIntegernow.add(poly.getCoefficient());
                } else if (bigIntegernow.compareTo(poly.getCoefficient()) > 0) {
                    bigIntegernow = bigIntegernow.subtract(poly.getCoefficient());
                } else {
                    bigIntegernow = poly.getCoefficient().subtract(bigIntegernow);
                    signnow = poly.getSign();
                }
            } else {
                ans.addAns(signnow,bigIntegernow,indexnow);
                signnow = poly.getSign();
                bigIntegernow = poly.getCoefficient();
                indexnow = poly.getIndex();
            }
        }
        if (bigIntegernow.compareTo(zero) != 0) {
            ans.addAns(signnow,bigIntegernow,indexnow);
        }
        ans.out();
    }

    /*public static void outFirst(String sign, BigInteger bigInteger, int index) {
        BigInteger big = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        if (bigInteger.compareTo(zero) == 0) {
            System.out.print(0);
        } else if (index == 0) {
            if (sign.equals("-")) {
                System.out.print(sign);
            }
            System.out.print(bigInteger);
        } else if (index == 1) {
            if (sign.equals("-")) {
                System.out.print(sign);
            }
            if (bigInteger.compareTo(big) == 0) {
                System.out.print("x");
            } else {
                System.out.print(bigInteger);
                System.out.print("*x");
            }
        } else {
            if (sign.equals("-")) {
                System.out.print(sign);
            }
            if (bigInteger.compareTo(big) == 0) {
                System.out.print("x**");
            } else {
                System.out.print(bigInteger);
                System.out.print("*x**");
            }
            System.out.print(index);
        }
    }

    public static void out(String sign, BigInteger bigInteger, int index) {
        BigInteger big = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        if (bigInteger.compareTo(zero) == 0) {
            System.out.print(sign);
            System.out.print(0);
        } else if (index == 0) {
            if (sign.equals("-")) {
                System.out.print(sign);
            }
            System.out.print(bigInteger);
        } else if (index == 1) {
            System.out.print(sign);
            if (bigInteger.compareTo(big) == 0) {
                System.out.print("x");
            } else {
                System.out.print(bigInteger);
                System.out.print("*x");
            }
        } else {
            System.out.print(sign);
            if (bigInteger.compareTo(big) == 0) {
                System.out.print("x**");
            } else {
                System.out.print(bigInteger);
                System.out.print("*x**");
            }
            System.out.print(index);
        }
    }*/
}