import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private BigInteger expo;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void setExpo(BigInteger expo) {
        this.expo = expo;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" +");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" +");
            }
        }
        if (!this.expo.equals(BigInteger.ONE)) {
            sb.append(" ");
            sb.append(expo);
            sb.append(" ^");
        }
        return sb.toString();
    }
}
