import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String expr = scanner.readLine();
        expr = expr.replaceAll(" ", "");
        expr = expr.replaceAll("\t", "");

        Lexer lexer = new Lexer(expr);
        Parser parser = new Parser(lexer);

        Expr res = parser.parseExpr();
        System.out.println(res);
    }

}
