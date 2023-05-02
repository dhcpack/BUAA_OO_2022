import java.math.BigInteger;
import java.util.ArrayList;

public class MulPoly {
    private Poly poly1;
    private Poly poly2;

    public MulPoly(Poly poly1, Poly poly2) {
        this.poly1 = poly1;
        this.poly2 = poly2;
    }

    public MulPoly() {

    }

    public Poly mul(Poly poly1, Poly poly2) {
        int i = 0;
        int j = 0;
        ArrayList<Term> mulTerms = new ArrayList<>();
        Term term = new Term();
        /*for (Term term1 : poly1.getTerms()) {
            for (Term term2 : poly2.getTerms()) {
                mulTerms.add(term.mulTerm(term1, term2));
            }
        }*/
        for (i = 0; i < poly1.getTerms().size(); i++) {
            for (j = 0; j < poly2.getTerms().size(); j++) {
                Term term1 = poly1.getTerms().get(i);
                Term term2 = poly2.getTerms().get(j);
                mulTerms.add(term.mulTerm(term1, term2));
            }
        }
        Poly poly = new Poly(mulTerms);
        return poly;
    }

    public Poly pow(Poly poly1, Poly poly2) {
        ArrayList<Term> tempTerms = new ArrayList<>();
        ArrayList<TriFactor> sinArray = new ArrayList<>();
        ArrayList<TriFactor> cosArray = new ArrayList<>();
        //0次方处理
        Term term = new Term(new BigInteger("0"), new BigInteger("1"), "+", sinArray, cosArray);
        tempTerms.add(term);
        Poly poly = new Poly(tempTerms);
        for (int i = 0; poly2.getTerms().get(0).getCoefTerm().compareTo(
                new BigInteger(String.valueOf(i))) > 0; i++) {
            //i < poly2.getTerms().get(0).getCoef()
            poly = mul(poly, poly1);
        }
        return poly;
    }
}
