public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    public int getPos() {
        return pos;
    }

    /**
     * 从输入的字符串中截取无符号数字
     *
     * @return String类型的无符号数字
     */
    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    /**
     * 从输入的字符串中截取括号内的表达式
     *
     * @return 表达式字符串
     */
    private String getExpression() {
        int x = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos));
        pos++;
        while (pos < input.length()) {
            if (String.valueOf(input.charAt(pos)).equals(")")) {
                x--;
            } else if (String.valueOf(input.charAt(pos)).equals("(")) {
                x++;
            }
            sb.append(input.charAt(pos));
            ++pos;
            if (x == 0) {
                break;
            }
        }

        return sb.toString();
    }

    /**
     * 从输入的字符串中截取下一个单元（正负符号，无符号数字，括号内表达式）
     * 并保存在curToken中
     */
    public void next() {
        if (pos == input.length()) {
            return;
        }
        deleteSpace();
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (String.valueOf(c).equals(")") | String.valueOf(c).equals("x")) {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (String.valueOf(c).equals("*")) {
            if (String.valueOf(input.charAt(pos + 1)).equals("*")) {
                curToken = "**";
                pos += 2;
            } else {
                curToken = String.valueOf(c);
                pos++;
            }
        } else if (String.valueOf(c).equals("(")) {
            curToken = getExpression();
        } else if (String.valueOf(c).equals("+") | String.valueOf(c).equals("-")) {
            curToken = addOrSub();
        } else {
            System.out.println("It's not a good input.The pos is " + pos);
        }
    }

    /**
     * 获得curToken中的数值
     *
     * @return 返回curToken
     */
    public String peek() {
        return this.curToken;
    }

    /**
     * 获得字符串中的正负号并将连续的正负号结合
     *
     * @return 正负号字符串
     */
    public String addOrSub() {
        boolean sysmbol = true;
        while (pos < input.length()) {
            if (String.valueOf(input.charAt(pos)).equals("+")) {
                sysmbol = sysmbol;
            } else if (String.valueOf(input.charAt(pos)).equals("-")) {
                sysmbol = !sysmbol;
            } else {
                break;
            }
            ++pos;
            deleteSpace();
        }
        if (sysmbol) {
            return "+";
        } else {
            return "-";
        }
    }

    /**
     * 跳过输入字符串中的空白字符
     */
    public void deleteSpace() {
        char c = input.charAt(pos);
        while (String.valueOf(c).equals(" ") | String.valueOf(c).equals("\t")) {
            pos++;
            if (pos < input.length()) {
                c = input.charAt(pos);
            } else {
                return;
            }
        }
    }
}