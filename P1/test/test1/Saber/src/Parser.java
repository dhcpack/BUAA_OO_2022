import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.merge(parseTerm());
        while (!lexer.peek().equals(")") && !lexer.isFull()) {
            expr.merge(parseTerm());
        }
        return expr;
    }

    public Expr parseTerm() {
        Expr expr = null;
        Term term = new Term();
        int symbol = 1;
        if (lexer.peek().equals("+")) {
            lexer.next();
        }
        if (lexer.peek().equals("-")) {
            lexer.next();
            symbol = -1;
        }

        if (lexer.peek().equals("(")) {
            lexer.next();
            expr = parseExpr();
        }
        else if (lexer.peek().equals("x")) {
            term.setIndex(1);
        }
        else if (lexer.peek().matches("\\d+")) {
            BigInteger temp = new BigInteger(lexer.peek());
            term.setFactor(term.getFactor().multiply(temp));
        }
        lexer.next();

        if (expr == null) {
            expr = new Expr(term);
        }

        expr = parsePower(expr);
        if (symbol == -1) {
            expr.reverse();
        }
        // can not reverse * and **
        if (lexer.peek().equals("*")) {
            lexer.next();
            expr.multi(parseTerm());
            if (lexer.peek().equals("*")) { // to prevent multiple calls in recursion
                lexer.next();
            }
        }
        return expr;
    }

    private Expr parsePower(Expr expr) {
        if (!lexer.peek().equals("**")) {
            return expr;
        }
        lexer.next();
        if (lexer.peek().equals("+")) {
            lexer.next();
        }
        int index = Integer.parseInt(lexer.peek());
        Term temp = new Term();
        Expr mult = new Expr(temp);
        for (int i = 0;i < index;i++) {
            mult.multi(expr);
        }
        lexer.next();
        return mult;
    }
}
