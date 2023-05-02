import expr.Expr;
import expr.Number;
import expr.PowerFunc;
import expr.Term;
import expr.Factor;
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        String sign0 = "+";
        if (lexer.judge()) {
            sign0 = lexer.peek();
            lexer.next();
        }
        Term term0 = parseTerm();
        term0.setSign(sign0);
        expr.addTerm(term0);
        while (lexer.judge()) {
            String sign = lexer.peek();
            lexer.next();
            Term term = parseTerm();
            term.setSign(sign);
            expr.addTerm(term);
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.judge()) {
            term.setSign(lexer.peek());
            lexer.next();
        }
        term.setFirst(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.judge()) {
                    lexer.next();
                }
                int index1 = Integer.parseInt(lexer.peek());
                lexer.next();
                Expr expr1 = (Expr) expr;
                expr1.setIndex(index1);
            }
            return expr;
        } else if (lexer.peek().equals("x")) {
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.judge()) {
                    lexer.next();
                }
                int index = Integer.parseInt(lexer.peek());
                lexer.next();
                return new PowerFunc(index);
            } else {
                return new PowerFunc(1);
            }
        } else {
            String sign = "+";
            if (lexer.judge()) {
                sign = lexer.peek();
                lexer.next();
            }
            BigInteger num = new BigInteger(lexer.peek());
            Number number = new Number(num);
            number.setSign(sign);
            lexer.next();
            return number;
        }
    }
}
