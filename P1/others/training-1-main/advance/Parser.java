import expr.Expr;
import expr.Factor;
import expr.Number;
import expr.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {  // expr表达式，里面是term项
        Expr expr = new Expr();
        expr.addTerm(parseTerm());

        while (lexer.peek().equals("+")) {
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {  // term项，里面是factor因子(包括表达式因子和数字因子)
        Term term = new Term();
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor()); /* TODO */
        }
        return term;
    }

    public Factor parseFactor() {  // factor因子
        if (lexer.peek().equals("(")) {  // 表达式因子
            lexer.next();
            Factor expr = parseExpr();  // 表达式因子解析表达式
            lexer.next();/* TODO */
            return expr;
        } else {  // 数字因子
            BigInteger num = new BigInteger(lexer.peek());/* TODO */
            lexer.next();
            return new Number(num);
        }
    }
}
