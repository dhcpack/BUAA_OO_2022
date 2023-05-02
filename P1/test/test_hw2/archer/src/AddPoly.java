import java.util.ArrayList;

public class AddPoly {
    private Poly poly1;
    private Poly poly2;

    public AddPoly(Poly poly1, Poly poly2) {
        this.poly1 = poly1;
        this.poly2 = poly2;
    }

    public AddPoly() {

    }

    public AddPoly(Poly poly1) {
        this.poly1 = poly1;
    }

    public Poly add(Poly poly1, Poly poly2) {
        ArrayList<Term> addTerms = new ArrayList<>();
        int i = 0;
        for (i = 0; i < poly1.getTerms().size(); i++) {
            addTerms.add(poly1.getTerms().get(i));
        }
        for (i = 0; i < poly2.getTerms().size(); i++) {
            addTerms.add(poly2.getTerms().get(i));
        }
        Poly poly = new Poly(addTerms);
        return poly;
    }

    public Poly pos(Poly poly1) {
        ArrayList<Term> posTerms = new ArrayList<>();
        posTerms = poly1.getTerms();
        Poly poly = new Poly(posTerms);
        return poly;
    }
}