import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class Main {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String expr = scanner.readLine();
        Lexer lexer = new Lexer(expr);
        Expression expression = new Expression(lexer);
        expression.simplify();
        String str = expression.turnString();
        Lexer lexer2 = new Lexer(str);
        Expression expression2 = new Expression(lexer2);
        expression2.simplify();
        System.out.println(expression2.turnString());
    }
}
