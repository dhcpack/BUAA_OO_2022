import java.util.ArrayList;

public class Poly {
    private ArrayList<Term> terms = new ArrayList<>();

    public Poly(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public Term getFirstTerm() {
        return terms.get(0);
    }
}
