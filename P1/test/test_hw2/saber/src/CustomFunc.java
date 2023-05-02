import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CustomFunc implements Factor {
    private static HashMap<String, F> customFuncs = new HashMap<>();

    public CustomFunc() {

    }

    public static void addF(String in) {
        F f = new F(in);
        customFuncs.put(in.substring(0, 1), f);
    }

    /*
    传入实参返回替换后的函数
     */
    public static String subs(String in) {
        String funcname = in.substring(0, 1);
        F f = customFuncs.get(funcname);
        String out = f.input;
        String[] arr = in.substring(2, in.length() - 1).split(",");
        for (int i = 0; i < arr.length; i = i + 1) {
            out = out.replaceAll(f.variables.get(i), "(" + arr[i] + ")");
        }
        return out;
    }

    /*
    保存函数名，形参的类
     */
    static class F {
        private String funcName;
        private ArrayList<String> variables = new ArrayList<>();
        private String input;

        public F(String in) {
            String newin = in;
            /*
            表达式预处理
            */
            newin = newin.replaceAll("[ \\t]+", "");
            newin = newin.replaceAll("\\*\\*", "^");
            //多符号的简化
            newin = newin.replaceAll("(\\+\\+\\+|\\+--|-\\+-|--\\+)", "+");
            newin = newin.replaceAll("(---|-\\+\\+|\\+-\\+|\\+\\+-)", "-");
            newin = newin.replaceAll("(--|\\+\\+)", "+");
            newin = newin.replaceAll("(\\+-|-\\+)", "-");
            //对减号的处理
            newin = newin.replaceAll("-\\(", "-1*(");
            newin = newin.replaceAll("-x", "-1*x");
            newin = newin.replaceAll("-y", "-1*y");
            newin = newin.replaceAll("-z", "-1*z");
            newin = newin.replaceAll("x", "a");
            newin = newin.replaceAll("y", "b");
            newin = newin.replaceAll("z", "c");
            newin = newin.replaceAll("sin", "u");
            newin = newin.replaceAll("cos", "v");
            newin = newin.replaceAll("sum", "w");
            newin = newin.replaceAll("-u", "-1*u");
            newin = newin.replaceAll("-v", "-1*v");
            newin = newin.replaceAll("-w", "-1*w");
            newin = newin.replaceAll("-f", "-1*f");
            newin = newin.replaceAll("-g", "-1*g");
            newin = newin.replaceAll("-h", "-1*h");
            newin = newin.replaceAll("-", "+-");

            funcName = newin.substring(0, 1);
            String[] arr = newin.substring(2, newin.indexOf("=") - 1).split(",");
            Collections.addAll(variables, arr);
            input = newin.substring(newin.indexOf("=") + 1);
        }
    }
}
