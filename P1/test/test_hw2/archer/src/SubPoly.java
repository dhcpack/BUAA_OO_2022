import java.util.ArrayList;

public class SubPoly {
    private Poly poly1;
    private Poly poly2;

    public SubPoly(Poly poly1, Poly poly2) {
        this.poly1 = poly1;
        this.poly2 = poly2;
    }

    public SubPoly() {

    }

    public SubPoly(Poly poly1) {
        this.poly1 = poly1;
    }

    public Poly sub(Poly poly1, Poly poly2) {  //poly1 - poly2
        ArrayList<Term> subTerms = new ArrayList<>();
        int i = 0;
        for (i = 0; i < poly1.getTerms().size(); i++) {
            subTerms.add(poly1.getTerms().get(i));
        }
        for (i = 0; i < poly2.getTerms().size(); i++) {
            if (poly2.getTerms().get(i).getSymbolTerm().equals("+")) {
                poly2.getTerms().get(i).setSymbolTerm("-");
            } else {
                poly2.getTerms().get(i).setSymbolTerm("+");
            }
            subTerms.add(poly2.getTerms().get(i));
        }
        Poly poly = new Poly(subTerms);
        return poly;
    }

    public Poly neg(Poly poly1) {
        ArrayList<Term> negTerms = new ArrayList<>();
        for (int i = 0; i < poly1.getTerms().size(); i++) {
            if (poly1.getTerms().get(i).getSymbolTerm().equals("+")) {
                poly1.getTerms().get(i).setSymbolTerm("-");
            } else {
                poly1.getTerms().get(i).setSymbolTerm("+");
            }
            negTerms.add(poly1.getTerms().get(i));
        }
        Poly poly = new Poly(negTerms);
        return poly;
    }
}
