import java.util.Stack;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    /*
    表达式词法解析
     */
    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    /*
    取出下一个整数
     */
    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    /*
    取出下一个字母
     */
    private String getLetter() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    /*
    解析下一个操作符或整数或字母
     */
    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {         //获得下一个正整数
            curToken = getNumber();
        } else if (Character.isLetter(c)) { //获得下一个字母
            curToken = getLetter();
            if (curToken.equals("w")) {     //获得sum函数及括号内的所有内容
                curToken = curToken + getWhole();
            } else if (curToken.equals("f") |
                    curToken.equals("g") | curToken.equals("h")) {    //获得自定义函数及括号内的实参
                curToken = curToken + getWhole();
            }
        } else if ("-".indexOf(c) != -1) {  //获得下一个负整数
            pos += 1;
            curToken = "-" + getNumber();
        } else if ("()+*^-,".indexOf(c) != -1) {    //运算符
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    /*
    当前的操作符|整数|字母
     */
    public String peek() {
        return this.curToken;
    }

    /*
    获取括号内的所有内容
     */
    public String getWhole() {
        Stack<Object> stack = new Stack<>();
        stack.push(input.charAt(pos));
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos));
        ++pos;
        while (!stack.isEmpty()) {
            char c = input.charAt(pos);
            if (c == ')') {
                stack.pop();
                sb.append(c);
            } else if (c == '(') {
                stack.push("(");
                sb.append(c);
            } else {
                sb.append(c);
            }
            ++pos;
        }
        return sb.toString();
    }
}
