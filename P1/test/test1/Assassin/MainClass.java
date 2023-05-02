import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String expr = scanner.readLine();
        Lexer lexer = new Lexer(expr);
        Main main = new Main();
        main.setInput(lexer.getInput());
        main.main();
    }
}
