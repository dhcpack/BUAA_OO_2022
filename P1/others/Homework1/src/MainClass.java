// 需要先将官方包中用到的工具类import进来

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expr.Expr;

public class MainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 一般读入模式下，读入一行字符串时使用readLine()方法，在这里我们使用其读入表达式
        String expr = scanner.readLine();
        expr = expr.replaceAll(" ", "");
        expr = expr.replaceAll("\t", "");


        Lexer lexer = new Lexer(preProcess(expr));
        Parser parser = new Parser(lexer);

        Expr res = parser.parseExpr();
        System.out.println(res);


        // 表达式括号展开相关的逻辑
    }

    public static String preProcess(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int left = 0;
        int right = 0;
        while (index < input.length()) {
            stringBuilder.append(input.charAt(index));
            if (input.charAt(index) == ')') {
                right = index;
                index++;
                if (index + 1 < input.charAt(index) && input.charAt(index) == '*' && input.charAt(
                        index + 1) == '*') {  // 如果出现括号加power
                    index += 2;
                    StringBuilder sb = new StringBuilder();  // 得到重复次数
                    while (index < input.length() && Character.isDigit(input.charAt(index))) {
                        sb.append(input.charAt(index));
                        index++;
                    }
                    for (int i = 1; i < Integer.parseInt(sb.toString()); i++) {
                        stringBuilder.append('*');
                        stringBuilder.append(input.substring(left, right + 1));
                    }
                }
            } else if (input.charAt(index) == '(') {
                left = index;
                index++;
            } else {
                index++;
            }
        }
        return stringBuilder.toString();
    }
}
