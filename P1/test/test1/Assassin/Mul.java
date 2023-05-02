import java.math.BigInteger;

public class Mul {
    private Expression right;
    private Expression left;

    public Mul(Expression right,Expression left) {
        this.right = right;
        this.left = left;
    }

    public Expression jieguo() {
        Expression expression = new Expression("0");
        for (Term term1 : this.right.getTerms()) {
            for (Term term2 : this.left.getTerms()) {
                Term term = new Term();
                BigInteger xishu = term1.getXishu().multiply(term2.getXishu());
                BigInteger cishu = term1.getCishu().add(term2.getCishu());
                term.setXishu(xishu);
                term.setCishu(cishu);
                expression.setTerms(term);
            }
        }
        expression.hebin();
        return expression;
    }

    public Expression jiafa() {
        Expression expression = new Expression("0");
        for (Term term2 : this.left.getTerms()) {
            Term term = new Term();
            BigInteger xishu = term2.getXishu();
            BigInteger cishu = term2.getCishu();
            term.setXishu(xishu);
            term.setCishu(cishu);
            expression.setTerms(term);
        }
        for (Term term1 : this.right.getTerms()) {
            Term term = new Term();
            BigInteger xishu = term1.getXishu();
            BigInteger cishu = term1.getCishu();
            term.setXishu(xishu);
            term.setCishu(cishu);
            expression.setTerms(term);
        }
        expression.hebin();
        return expression;
    }
}
