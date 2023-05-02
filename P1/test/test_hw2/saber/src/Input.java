import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class Input {
    public static String getIn() {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 获取自定义函数个数
        int cnt = scanner.getCount();

        // 读入自定义函数
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            //System.out.println(func);//输出自定义函数

            func = func.replaceAll("[ \\t]+", "");
            // 存储或者解析逻辑
            CustomFunc.addF(func);
        }

        // 读入最后一行表达式
        String input = scanner.readLine();
        //System.out.println(input);//输出待化简表达式

        /*
         表达式预处理
         */
        input = input.replaceAll("[ \\t]+", "");
        input = input.replaceAll("\\*\\*", "^");
        //多符号的简化
        input = input.replaceAll("(\\+\\+\\+|\\+--|-\\+-|--\\+)", "+");
        input = input.replaceAll("(---|-\\+\\+|\\+-\\+|\\+\\+-)", "-");
        input = input.replaceAll("(--|\\+\\+)", "+");
        input = input.replaceAll("(\\+-|-\\+)", "-");
        //对减号的处理
        input = input.replaceAll("-\\(", "-1*(");
        input = input.replaceAll("-x", "-1*x");
        input = input.replaceAll("sin", "u");
        input = input.replaceAll("cos", "v");
        input = input.replaceAll("sum", "w");
        input = input.replaceAll("-u", "-1*u");
        input = input.replaceAll("-i", "-1*i");
        input = input.replaceAll("-v", "-1*v");
        input = input.replaceAll("-w", "-1*w");
        input = input.replaceAll("-f", "-1*f");
        input = input.replaceAll("-g", "-1*g");
        input = input.replaceAll("-h", "-1*h");
        input = input.replaceAll("-", "+-");
        return input;
    }
}
