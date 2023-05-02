import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String str = scanner.readLine();
        str = str.replaceAll("\\s*", "");
        InputData strInput = new InputData(str);
        Analyzer analyzerNow = new Analyzer(strInput);
        Result output = analyzerNow.readExpr();
        output.print();
    }
}
