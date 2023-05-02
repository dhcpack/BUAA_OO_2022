import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class MainClass {
    public static void main(String[] args) {
        String input = Input.getIn();

        //表达式解析
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        //System.out.println(expr.toString());

        //转化为后缀形式
        List<String> arr = Arrays.asList(expr.toString().split(" "));
        //System.out.println(arr);//输出后缀表达式列表形式

        //计算
        Expression finalExp = calculate(arr);
        //System.out.println(finalExp.getExpressions());//输出表达式hash表形式

        //输出
        System.out.println(Output.string(finalExp));
    }

    /*
    计算方法 利用栈将后缀表达式进行计算
     */
    public static Expression calculate(List<String> arr) {
        Stack<Object> stack = new Stack<>();
        for (String item : arr) {
            if (item.matches("[*+^]")) {
                Expression e2 = (Expression) stack.pop();
                Expression e1 = (Expression) stack.pop();
                switch (item) {
                    case "+":
                        e1.add(e2);
                        break;
                    case "*":
                        e1.mul(e2);
                        break;
                    case "^":
                        e1.pow(e2);
                        break;
                    default:
                        break;
                }
                stack.push(e1);
            } else if (item.matches("(sin|cos)")) {
                Expression e = (Expression) stack.pop();
                switch (item) {
                    case "sin":
                        e.sin();
                        break;
                    case "cos":
                        e.cos();
                        break;
                    default:
                        break;
                }
                stack.push(e);
            } else {
                Expression e = new Expression(item);
                stack.push(e);
            }
        }
        return (Expression) stack.pop();
    }
}
