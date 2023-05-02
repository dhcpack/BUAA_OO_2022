import expr.Expr;
import expr.Factor;
import expr.Number;
import expr.Term;
import expr.Pow;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();

        int sign = getNumSign();
        expr.addTerm(parseTerm(sign));

        while ("+-".contains(lexer.peek())) {
            sign = getNumSign();
            expr.addTerm(parseTerm(sign));
        }
        return expr;
    }

    public Term parseTerm(int sign) {
        Term term = new Term(sign);
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("x")) {
            Pow pow = new Pow();                    // 变量因子
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                int sign = getNumSign();
                int num = sign * Integer.parseInt(lexer.peek());
                lexer.next();
                pow.addTerms(num);
            } else {
                pow.addTerms(1);
            }
            return pow;
        } else if (lexer.peek().equals("(")) {      // 表达式因子
            lexer.next();
            Expr expr = parseExpr();
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                int sign = getNumSign();
                int num = sign * Integer.parseInt(lexer.peek());
                lexer.next();
                expr.setTerms(expr.calcPow(num));
            }
            return expr;
        } else {                                    // 数字
            int sign = getNumSign();
            BigInteger num = new BigInteger(lexer.peek()).multiply(BigInteger.valueOf(sign));
            lexer.next();
            return new Number(num);
        }
    }

    private int getNumSign() {
        int sign = 1;
        while ("+-".contains(lexer.peek())) {
            sign = lexer.peek().equals("-") ? -sign : sign;
            lexer.next();
        }
        return sign;
    }

}
