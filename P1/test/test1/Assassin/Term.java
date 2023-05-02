import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term  {
    private BigInteger cishu;
    private BigInteger xishu;
    private String input;
    private ArrayList<Factor> factors = new ArrayList<>();
    private final Pattern case1 = Pattern.compile("[+-]x");
    private final Pattern case2 = Pattern.compile("[+-]{2}");
    private final Pattern case3 = Pattern.compile("[+-]\\(");

    public Term() {

    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setXishu(BigInteger xishu) {
        this.xishu = xishu;
    }

    public BigInteger getXishu() {
        return this.xishu;
    }

    public void setCishu(BigInteger cishu) {
        this.cishu = cishu;
    }

    public Term(String input) {
        this.setInput(input);
        this.didi();
        this.setXishu(wayXi());
        this.setCishu(wayCi());
    }

    public void didi() {
        String[] b = this.input.split("\\*");
        Matcher matcher1 = case1.matcher(b[0]);
        Matcher matcher2 = case2.matcher(b[0]);
        Factor temp;
        if (matcher1.find() || matcher2.find()) {
            if (b[0].charAt(0) == '-')
            {
                temp = new Factor(b[0].substring(1));
                temp.setXishu(temp.getXishu().negate());
                factors.add(temp);
            }
            else {
                temp = new Factor(b[0].substring(1));
                factors.add(temp);
            }
        }
        else {
            factors.add(new Factor(b[0]));
        }
        for (int i = 1;i < b.length;i++) {
            factors.add(new Factor(b[i]));
        }
    }

    public BigInteger wayXi() {
        BigInteger mul = new BigInteger("1");
        for (Factor factor : factors) {
            mul = mul.multiply(factor.getXishu());
        }
        return mul;
    }

    public BigInteger wayCi() {
        BigInteger sum = new BigInteger("0");
        for (Factor factor : factors) {
            sum = sum.add(factor.getCishu());
        }
        return sum;
    }

    public BigInteger getCishu() {
        return this.cishu;
    }
}
