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
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (pos + 1 < input.length() && input.charAt(pos) == '*' && input.charAt(
                pos + 1) == '*') {
            curToken = "**";
            pos += 2;
        } else if ("+-*()x".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos += 1;
        }
    }

    public String peek() {
        return this.curToken;
    }
}