import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private String input;
    private ArrayList<Term> terms = new ArrayList<>();
    private final Pattern case1 = Pattern.compile("\\d+[+-]");
    private final Pattern case2 = Pattern.compile("x[+-]");
    private final Pattern case3 = Pattern.compile("[+-]{2}x");
    private final Pattern case4 = Pattern.compile("[+-]{3}");
    private final Pattern case6 = Pattern.compile("[+-]{2}\\(");

    public ArrayList<Term> getTerms() {
        return this.terms;
    }

    public Expression(String input) {
        this.setInput(input);
        this.didi();
        this.hebin();
    }

    public void setTerms(Term term) {
        this.terms.add(term);
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void didi() {
        Matcher matcher1 = case1.matcher(this.input);
        StringBuilder stringBuilder = new StringBuilder(input);
        int a = 0;
        int b = 0;
        while (matcher1.find()) {
            stringBuilder.insert(matcher1.end() - 1 + a,"#");
            a++;
        }
        String k = stringBuilder.toString();
        StringBuilder stringBuilder1 = new StringBuilder(k);
        Matcher matcher2 = case2.matcher(k);
        while (matcher2.find()) {
            stringBuilder1.insert(matcher2.end() - 1 + b,"#");
            b++;
        }
        String s = stringBuilder1.toString();
        String[] q = s.split("#");
        for (int i = 0; i < q.length;i++) {
            Matcher matcher3 = case3.matcher(q[i]);
            Matcher matcher4 = case4.matcher(q[i]);
            if (matcher3.find() || matcher4.find()) {
                if (q[i].charAt(0) == '-') {
                    Term term = new Term(q[i].substring(1));
                    term.setXishu(term.getXishu().negate());
                    terms.add(term);
                }
                else {
                    terms.add(new Term(q[i].substring(1)));
                }
            }
            else {
                terms.add(new Term(q[i]));
            }
        }
    }

    public String ptr() {
        StringBuilder stringBuilder = new StringBuilder();
        int k = 0;
        for (Term term : terms) {
            if (k == 0) {
                stringBuilder.append(term.getXishu().toString() +
                        "*x^" + term.getCishu().toString());
                k = 1;
            }
            else {
                stringBuilder.append("+" + term.getXishu().toString() +
                        "*x^" + term.getCishu().toString());
            }
        }
        return stringBuilder.toString();
    }

    public void hebin() {
        int i;
        int j;
        for (i = 0;i < terms.size() - 1;i++) {
            for (j = i + 1;j < terms.size();j++) {
                if (terms.get(i).getCishu().compareTo(terms.get(j).getCishu()) == 0) {
                    terms.get(i).setXishu(terms.get(i).getXishu().add(terms.get(j).getXishu()));
                    terms.remove(j);
                    j = j - 1;
                }
            }
        }
    }

    public void qufan() {
        for (Term term : this.terms) {
            term.setXishu(term.getXishu().negate());
        }
    }

    public String zz() {
        String s = this.ptr();
        return s.replaceAll("\\^","**");
    }

    public String reptr() {
        this.zhao();
        StringBuilder stringBuilder = new StringBuilder();
        BigInteger zone = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        BigInteger fone = new BigInteger("-1");
        BigInteger two = new BigInteger("2");
        for (Term term : terms) {
            BigInteger cishu = term.getCishu();
            BigInteger xishu = term.getXishu();
            if (xishu.compareTo(zero) == 0) {
                continue;
            }
            else if (xishu.compareTo(zone) == 0) {
                if (cishu.compareTo(zero) == 0) {
                    stringBuilder.append("+1");
                }
                else if (cishu.compareTo(zone) == 0) {
                    stringBuilder.append("+x");
                }
                else if (cishu.compareTo(two) == 0) {
                    stringBuilder.append("+x*x");
                }
                else {
                    stringBuilder.append("+x**" + cishu.toString());
                }
            }
            else if (xishu.compareTo(fone) == 0) {
                if (cishu.compareTo(zero) == 0) {
                    stringBuilder.append("-1");
                }
                else if (cishu.compareTo(zone) == 0) {
                    stringBuilder.append("-x");
                }
                else if (cishu.compareTo(two) == 0) {
                    stringBuilder.append("-x*x");
                }
                else {
                    stringBuilder.append("-x**" + cishu.toString());
                }
            }
            else {
                stringBuilder.append(wo(term));
            }
        }
        String s = stringBuilder.toString();
        s = s.replaceAll("^\\+","");
        if (s.equals(""))
        {
            return "0";
        }
        else
        {
            return s;
        }
    }

    public String wo(Term term) {
        BigInteger zone = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        BigInteger fone = new BigInteger("-1");
        BigInteger two = new BigInteger("2");
        BigInteger cishu = term.getCishu();
        BigInteger xishu = term.getXishu();
        if (xishu.compareTo(zero) > 0) {
            if (cishu.compareTo(zero) == 0) {
                return ("+" + xishu.toString());
            }
            else if (cishu.compareTo(zone) == 0) {
                return ("+" + xishu.toString() + "*x");
            }
            else if (cishu.compareTo(two) == 0) {
                return ("+" + xishu.toString() + "*x*x");
            }
            else {
                return ("+" + xishu.toString() + "*x**" + cishu.toString());
            }
        }
        else {
            if (cishu.compareTo(zero) == 0) {
                return (xishu.toString());
            }
            else if (cishu.compareTo(zone) == 0) {
                return (xishu.toString() + "*x");
            }
            else if (cishu.compareTo(two) == 0) {
                return (xishu.toString() + "*x*x");
            }
            else {
                return (xishu.toString() + "*x**" + cishu.toString());
            }
        }
    }

    public void zhao() {
        BigInteger zero = new BigInteger("0");
        if (terms.get(0).getXishu().compareTo(zero) > 0)
        {
            return;
        }
        for (int i = 1; i < terms.size();i++) {
            if (terms.get(i).getXishu().compareTo(zero) > 0)
            {
                Collections.swap(terms,0,i);
                return;
            }
        }
    }
}
