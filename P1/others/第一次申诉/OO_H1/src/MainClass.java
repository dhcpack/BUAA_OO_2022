import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expr.Expr;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String expr = scanner.readLine();
        Lexer lexer = new Lexer(expr.replaceAll("[ \t]", ""));
        Parser parser = new Parser(lexer);
        Expr res = parser.parseExpr();
        System.out.println(res.toString());
    }
}
