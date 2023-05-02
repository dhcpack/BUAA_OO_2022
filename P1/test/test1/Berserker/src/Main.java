import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expression.Expression;
import parse.Lexer;
import parse.Parse;
import poly.Polynomial;

//import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        ExprInput input = new ExprInput(ExprInputMode.NormalMode);
        String content = input.readLine();
        //Scanner scanner = new Scanner(System.in);
        //String content = scanner.nextLine();

        Lexer lexer = new Lexer(content);
        Parse parse = new Parse(lexer);

        Expression expression = parse.parseExpression();
        Polynomial polynomial = expression.toPolynomial();
        polynomial.merge();
        System.out.println(polynomial);
    }
}
