import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class Main {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        Lexer lexer = new Lexer(scanner.readLine());
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        System.out.println(expr);
    }
}
