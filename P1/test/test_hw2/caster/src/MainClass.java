import parser.Lexer;
import parser.Parser;
import sentence.CustomFunct;
import sentence.CustomFunctDefs;
import sentence.Expre;

import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        Lexer lexer;
        Parser parser;

        int cases = scanner.getCount();
        CustomFunctDefs defs = new CustomFunctDefs();
        for (int i = 0; i < cases; ++i) {
            lexer = new Lexer(scanner.readLine());
            parser = new Parser(lexer);
            CustomFunct funct = parser.parseCustomFunctDef();
            defs = defs.add(funct);
        }
        lexer = new Lexer(scanner.readLine());
        parser = new Parser(lexer, defs);
        Expre expression = parser.parseExpre();
        System.out.println(expression.toMathExpre().toSimpleString());
    }
}