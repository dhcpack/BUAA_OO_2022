public class Lexer {
    private final String expr;
    private int pos = 0;
    private String now;

    public void next() {
        if (pos == expr.length()) {
            return;
        }

        char c1 = expr.charAt(pos);
        char c2 = pos + 1 < expr.length() ? expr.charAt(pos + 1) : 'E';
        //String s = String.valueOf(c1) + c2;

        if (Character.isDigit(c1)) {
            now = getNumber();
        } else if (pos + 1 < expr.length() && "*".indexOf(c1) != -1 && "*".indexOf(c2) != -1) {
            pos += 2;
            now = "**";
        } else if ("()+-*x".indexOf(c1) != -1) {
            pos += 1;
            now = String.valueOf(c1);
        }
    }

    public String peek() {
        return this.now;
    }

    public String getNumber() {
        StringBuilder num = new StringBuilder();
        while (pos < expr.length() && Character.isDigit(expr.charAt(pos))) {
            num.append(expr.charAt(pos));
            pos += 1;
        }
        return num.toString();
    }

    public Lexer(String expr) {
        this.expr = expr;
        this.next();
    }
}
