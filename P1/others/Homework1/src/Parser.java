import expr.Number;
import expr.Expr;
import expr.Term;
import expr.Factor;
import expr.Power;
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    private int sign = 1;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        while ("+-".contains(lexer.peek())) {
            if (lexer.peek().equals("-")) {
                sign = -sign;
            }
            lexer.next();
        }
    }

    public Expr parseExpr() {  // expr表达式，里面是term项，记录方式为HashMap<Integer, BigInteger>
        Expr expr = new Expr();
        expr.mergeTerms(parseTerm());

        while ("+-".contains(lexer.peek())) {
            sign = 1;
            while ("+-".contains(lexer.peek())) {
                if (lexer.peek().equals("-")) {
                    sign = -sign;
                }
            }
            lexer.next();
            expr.mergeTerms(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {  // term项，里面是factor因子(包括表达式因子，数字因子，幂函数因子)
        Term term = new Term(sign);
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {  // factor因子
        if (lexer.peek().equals("(")) {  // 表达式因子
            lexer.next();
            Factor expr = parseExpr();  // 表达式因子解析表达式
            lexer.next();
            // if(lexer.peek().equals("**")){
            //     return
            // }
            return expr;
        } else if (lexer.peek().equals("x")) {
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                int degree = Integer.parseInt(lexer.peek());
                lexer.next();
                return new Power(degree);
            } else {
                return new Power(1);
            }
        } else {  // 数字因子
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        }
    }
}
