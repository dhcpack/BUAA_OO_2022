public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < input.length();i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) || "(+-*)x".indexOf(c) != -1) {
                sb.append(c);
            }
        }
        this.input = sb.toString();
        this.next();
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    public String getSymbol() {
        int symbol = 1;
        while (pos < input.length() && "+-".indexOf(input.charAt(pos)) != -1) {
            if (input.charAt(pos) == '-') {
                symbol *= -1;
            }
            pos++;
        }
        return (symbol == 1) ? "+" : "-";
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        }
        else if ("+-".indexOf(c) != -1) {
            curToken = getSymbol();
        }
        else if ("()x".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            ++pos;
        }
        else if (c == '*') {
            if (pos + 1 < input.length() && input.charAt(pos + 1) == '*') {
                curToken = "**";
                pos += 2;
            }
            else {
                curToken = "*";
                ++pos;
            }
        }
    }

    public String peek() {
        return curToken;
    }

    public boolean isFull() {
        return (pos == input.length());
    }
}
