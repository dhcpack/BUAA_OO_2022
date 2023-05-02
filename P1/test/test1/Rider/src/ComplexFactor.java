public class ComplexFactor implements Factor {
    private Expression expression;
    private int pow;

    public int getPow() {
        return pow;
    }

    public Expression getExpression() {
        return expression;
    }

    public ComplexFactor(Lexer lexer) {
        String str = lexer.peek();
        Lexer nextLexer = new Lexer(str.substring(1, str.length() - 1));
        expression = new Expression(nextLexer);
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            pow = Integer.parseInt(lexer.peek());
            lexer.next();
        } else {
            pow = 1;
        }
    }

    @Override
    public String turnString() {
        if (pow == 1) {
            return "(" + expression.turnString() + ")";
        } else {
            return "(" + expression.turnString() + ")" + "**" + pow;
        }
    }

    @Override
    public void simplify() {
        expression.simplify();
        Lexer lexer = new Lexer("1");
        Expression answer = new Expression(lexer);
        int i;
        for (i = 0; i < pow; i++) {
            answer = answer.multExpression(expression);
        }
        expression = answer;
        pow = 1;
    }
}
