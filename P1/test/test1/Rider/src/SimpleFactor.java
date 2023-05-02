public class SimpleFactor implements Factor {
    private int pow;

    public void sameAs(SimpleFactor simpleFactor) {
        pow = simpleFactor.getPow();
    }

    public SimpleFactor(Lexer lexer) {
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

    public int getPow() {
        return pow;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    @Override
    public String turnString() {
        if (pow == 0) {
            return "1";
        } else if (pow == 1) {
            return "x";
        } else if (pow == 2) {
            return "x*x";
        } else {
            return "x**" + Integer.toString(pow);
        }
    }

    @Override
    public void simplify() {

    }
}
