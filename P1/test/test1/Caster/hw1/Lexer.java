public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        while (c == ' ' || c == '\t') {
            pos++;
            c = input.charAt(pos);
        }
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("()+-x".indexOf(c) != -1) {
            pos++;
            curToken = String.valueOf(c);
        } else if (c == '*') {
            pos++;
            c = input.charAt(pos);
            if (c == '*') {
                pos++;
                curToken = "**";
            } else {
                curToken = "*";
            }
        }
    }

    public String peek() {
        return curToken;
    }

    public int getPos() {
        return pos;
    }

    public boolean judge() {
        return peek().equals("+") || peek().equals("-");
    }
}
