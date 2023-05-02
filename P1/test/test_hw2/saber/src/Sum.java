import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sum implements Factor {
    private Expr expr; //可能为表达式

    public Sum(String in) {
        String mode = "\\(i,(\\d+),(\\d+),(\\S+)\\)";
        Pattern pattern = Pattern.compile(mode);
        Matcher matcher = pattern.matcher(in);
        if (matcher.find()) {
            BigInteger s = new BigInteger(matcher.group(1));
            BigInteger e = new BigInteger(matcher.group(2));
            String newFactor = matcher.group(3);
            if (e.compareTo(s) < 0) {
                Term term = new Term();
                term.addFactor(new Const(BigInteger.ZERO));
                this.expr.addTerm(term);
            } else {
                StringBuilder re = new StringBuilder();
                for (BigInteger i = s; i.compareTo(e) <= 0; i = i.add(BigInteger.ONE)) {
                    re.append("+");
                    re.append(newFactor.replaceAll("i", "(" + String.valueOf(i) + ")"));
                }
                Lexer lexer = new Lexer(re.toString());
                Parser parser = new Parser(lexer);
                this.expr = parser.parseExpr();
            }
        }
    }

    public Factor getExpr() {
        return this.expr;
    }

    public String toString() {
        return expr.toString();
    }
}
