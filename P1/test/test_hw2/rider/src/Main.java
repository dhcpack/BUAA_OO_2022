import ast.Node;
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import parse.Lexer;
import parse.Parser;
import poly.Polynomial;

import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int n = scanner.getCount();

        Parser parser = new Parser();

        for (int i = 0; i < n; i++)
        {
            parser.addFunction(scanner.readLine());
        }

        parser.setLexer(new Lexer(scanner.readLine()));

        Node tree = parser.parseExpression();

        Polynomial polynomial = tree.toPoly(new HashMap<>());

        System.out.println(polynomial);
        
    }
}
