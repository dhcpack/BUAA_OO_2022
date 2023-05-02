import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expr.Expr;
import lex.Lexer;
import parser.Parser;
import poly.Polynomial;

public class Main {
    public static void main(String[] args) {
        ExprInput exprInput = new ExprInput(ExprInputMode.NormalMode);
        String input = exprInput.readLine();
        input = input.replaceAll("\\*\\*", "^");

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        Polynomial ans = expr.toPolynomial();
        ans.mergePoly();
        System.out.println(ans);
    }
}
