import java.util.ArrayList;

public class Main {
    private String input;

    public void setInput(String input) {
        this.input = input;
    }

    public void main() {
        Kop kop = new Kop();
        kop.setInput(this.input);
        kop.setTerms();
        int j;
        ArrayList<Cop> cops = new ArrayList<>();
        for (String s : kop.getKops()) {
            Cop cop = new Cop();
            cop.setInput(s);
            cop.setCops();
            cops.add(cop);
        }
        Expression sum = new Expression("0");
        for (Cop cop : cops) {
            Expression mu = new Expression("1");
            for (String o : cop.getFactors()) {
                String temp;
                int num = 0;
                Expression oo;
                if (o.contains("("))
                {
                    StringBuilder stringBuilder = new StringBuilder(o);
                    for (j = 0;j < stringBuilder.length();j++) {
                        if (stringBuilder.charAt(j) == '-') {
                            num = num + 1;
                        }
                        if (stringBuilder.charAt(j) == '(') {
                            break;
                        }
                    }
                    temp = stringBuilder.substring(j + 1,stringBuilder.length() - 1);
                    oo = new Expression(temp);
                    if (num == 1 || num == 3)
                    {
                        oo.qufan();
                    }
                }
                else {
                    oo = new Expression(o);
                }
                Mul mul;
                mul = new Mul(mu,oo);
                mu = mul.jieguo();
            }
            Mul su;
            su = new Mul(sum,mu);
            sum = su.jiafa();
        }
        System.out.println(sum.reptr());
    }
}
