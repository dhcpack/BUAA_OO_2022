import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int pos;
    private String curToken;
    private final Pattern exprfactorPattern = Pattern.compile("\\(([^()]+)\\)\\^\\+?(\\d*)");
    private final Pattern case1 = Pattern.compile("\\(([^()]+)\\)");

    public String getInput() {
        return this.input;
    }

    public Lexer(String input) {
        String s = input.replaceAll("[ \t]","");
        String j = s.replaceAll("\\*\\*","^");
        Matcher matcher = exprfactorPattern.matcher(j);
        while (matcher.find()) {
            j = j.replaceFirst("\\(([^()$]+)\\)\\^\\+?(\\d*)",
                    way1(matcher.group(1),matcher.group(2)));
        }
        this.input = j;
    }

    public String way1(String a, String b) {
        int n;
        String c = "";
        int q = 0;
        if (b == "") {
            n = 0;
        }
        else {
            n = Integer.parseInt(b);
        }
        if (n == 0)
        {
            return "1";
        }
        for (int i = 1;i <= n;i++) {
            if (q == 0) {
                c = c + "(" + a + ")";
                q = 1;
            }
            else {
                c = c + "*" + "(" + a + ")";
            }
        }
        return c;
    }
}
