package polynomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class Polynomial {
    private final ArrayList<Poly> polies = new ArrayList<>();

    public void addPoly(Poly p) {
        polies.add(p);
    }

    public void copy(Polynomial polynomial) {
        ArrayList<Poly> p = polynomial.getPolies();
        this.polies.addAll(p);
    }

    public ArrayList<Poly> getPolies() {
        return polies;
    }

    public void addPolynomial(Polynomial x) {
        ArrayList<Poly> xx = x.getPolies();
        polies.addAll(xx);
    }

    public void multPolynomial(Polynomial x) {
        ArrayList<Poly> xx = x.getPolies();
        ArrayList<Poly> zz = new ArrayList<>(xx);
        ArrayList<Poly> yy = new ArrayList<>(polies);
        ArrayList<Poly> ans = new ArrayList<>();
        for (Poly p1 : zz) {
            for (Poly p2 : yy) {
                BigInteger p1Coefficient = p1.getCoefficient();
                BigInteger p2Coefficient = p2.getCoefficient();
                int p1Index = p1.getIndex();
                int p2Index = p2.getIndex();
//                System.out.print(p1.getSign());
//                System.out.print(p1Coefficient);
//                System.out.println(p1.getIndex());
//                System.out.print(p2.getSign());
//                System.out.print(p2Coefficient);
//                System.out.println(p2.getIndex());
//                System.out.println("");
                if (p1.getSign().equals(p2.getSign())) {
                    Poly p = new Poly(p1Coefficient.multiply(p2Coefficient),p1Index + p2Index,"+");
                    ans.add(p);
                } else {
                    Poly p = new Poly(p1Coefficient.multiply(p2Coefficient),p1Index + p2Index,"-");
                    ans.add(p);
                }
            }
        }
        polies.clear();
        polies.addAll(ans);
    }
}
