import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Expr {
    private ArrayList<Term> terms = new ArrayList<>();

    public Expr() {
    }

    public Expr(Term term) {
        this.terms.add(term);
    }

    public Expr(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public void merge(Expr other) {
        for (Term term1 : other.terms) {
            boolean isMerged = false;
            for (Term term2 : this.terms) {
                if (term1.getIndex() == term2.getIndex()) {
                    term2.setFactor(term1.getFactor().add(term2.getFactor()));
                    isMerged = true;
                    break;
                }
            }
            if (!isMerged) {
                this.terms.add(term1);
            }
        }
    }

    public void multi(Expr other) {
        ArrayList<Term> temp = new ArrayList<>();


        for (Term term1 : other.terms) {
            for (Term term2 : this.terms) {
                Term tempTerm = new Term();
                tempTerm.setFactor(term1.getFactor().multiply(term2.getFactor()));
                tempTerm.setIndex(term1.getIndex() + term2.getIndex());
                temp.add(tempTerm);
            }
        }
        this.terms.clear();
        Expr tempExpr = new Expr(temp);
        merge(tempExpr);
    }

    public String toString() {
        boolean head = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < terms.size();i++) {
            if (terms.get(i).getFactor().compareTo(BigInteger.ZERO) > 0) {
                Collections.swap(terms,0,i);
                break;
            }
        }
        for (Term term : terms) {
            String factor = term.getFactor().toString();
            if (factor.equals("0")) {
                continue;
            }
            if (factor.charAt(0) != '-' && !head) {
                sb.append("+");
            }
            head = false;
            if (term.getIndex() == 0) {
                sb.append(factor);
            }
            else {
                if (factor.equals("1")) {
                    sb.append("x");
                }
                else if (factor.equals("-1")) {
                    sb.append("-x");
                }
                else {
                    sb.append(factor);
                    sb.append("*x");
                }
                if (term.getIndex() != 1) {
                    if (term.getIndex() != 2) {
                        sb.append("**");
                        sb.append(term.getIndex());
                    }
                    else {
                        sb.append("*x");
                    }
                }
            }
        }
        if (sb.toString().equals("")) {
            sb.append("0");
        }
        return sb.toString();
    }

    public void reverse() {
        for (Term term : terms) {
            BigInteger temp = BigInteger.valueOf(-1);
            term.setFactor(term.getFactor().multiply(temp));
        }
    }
}
