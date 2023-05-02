import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factor {
    private String input;
    private BigInteger xishu;
    private BigInteger cishu;

    private final Pattern factorpattern = Pattern.compile("x\\^\\+?(\\d+)");

    public Factor(String input) {
        if (input.contains("x")) {
            if (input.equals("x")) {
                this.xishu = new BigInteger("1");
                this.cishu = new BigInteger("1");
            }
            else {
                Matcher matcher = factorpattern.matcher(input);
                matcher.find();
                this.xishu = new BigInteger("1");
                this.cishu = new BigInteger(matcher.group(1));
            }
        }
        else {
            this.cishu = new BigInteger("0");
            this.xishu = new BigInteger(input);
        }
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setXishu(BigInteger xishu) {
        this.xishu = xishu;
    }

    public void setCishu(BigInteger cishu) {
        this.cishu = cishu;
    }

    public String getInput() {
        return this.input;
    }

    public BigInteger getXishu() {
        return this.xishu;
    }

    public BigInteger getCishu() {
        return this.cishu;
    }
}
