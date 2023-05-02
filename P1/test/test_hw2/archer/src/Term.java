import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Comparable<Term> {
    private BigInteger indexTerm;
    private BigInteger coefTerm;
    private String symbolTerm;
    //后续可对sinArray中每个Tri添加指数变量 方便合并
    private ArrayList<TriFactor> sinArray;
    private ArrayList<TriFactor> cosArray;

    public Term(BigInteger index, BigInteger coef, String symbol, ArrayList<TriFactor> sinArray,
                ArrayList<TriFactor> cosArray) {
        this.indexTerm = index;
        this.coefTerm = coef;
        this.symbolTerm = symbol;   //根据具体op修改
        this.sinArray = sinArray;
        this.cosArray = cosArray;
    }

    public Term() {

    }

    public BigInteger getIndexTerm() {
        return indexTerm;
    }

    public BigInteger getCoefTerm() {
        return coefTerm;
    }

    public String getSymbolTerm() {
        return symbolTerm;
    }

    public ArrayList<TriFactor> getSinArray() {
        return sinArray;
    }

    public ArrayList<TriFactor> getCosArray() {
        return cosArray;
    }

    public void setSymbolTerm(String symbolTerm) {
        this.symbolTerm = symbolTerm;
    }

    public void setIndexTerm(BigInteger indexTerm) {
        this.indexTerm = indexTerm;
    }

    public void setCoefTerm(BigInteger coefTerm) {
        this.coefTerm = coefTerm;
    }

    public Term mulTerm(Term term1, Term term2) {
        //get symbol
        String symbol;
        if (term1.getSymbolTerm().equals("+") && term2.getSymbolTerm().equals("+")) {
            symbol = "+";
        } else if (term1.getSymbolTerm().equals("-") && term2.getSymbolTerm().equals("-")) {
            symbol = "+";
        } else if (term1.getSymbolTerm().equals("+") && term2.getSymbolTerm().equals("-")) {
            symbol = "-";
        } else {
            symbol = "-";
        }
        //将sin cos加入新的term的array中
        ArrayList<TriFactor> sinArray = new ArrayList<>();
        ArrayList<TriFactor> cosArray = new ArrayList<>();

        addSin(term1.getSinArray(), sinArray);
        addCos(term1.getCosArray(), cosArray);
        addSin(term2.getSinArray(), sinArray);
        addCos(term2.getCosArray(), cosArray);
        //get new coef
        BigInteger coef = term1.getCoefTerm().multiply(term2.getCoefTerm());
        //get new index
        BigInteger index = term1.getIndexTerm().add(term2.getIndexTerm());
        Term term = new Term(index, coef, symbol, sinArray, cosArray);
        return term;
    }

    public static void addSin(ArrayList<TriFactor> termSinArray, ArrayList<TriFactor> sinArray) {
        for (int i = 0; i < termSinArray.size(); i++) {
            TriFactor triFactor = termSinArray.get(i);
            sinArray.add(triFactor);
            /*int flag = 0;
            for (TriFactor item : sinArray) {
                if (item.getSymbolTri().equals(triFactor.getSymbolTri()) &&
                        item.getCoefTri().compareTo(triFactor.getCoefTri()) == 0 &&
                        item.getIndexTri().compareTo(triFactor.getIndexTri()) == 0) {
                    item.setIsIndex(item.getIsIndex() + 1);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                sinArray.add(triFactor);
            }*/
        }
    }

    public static void addCos(ArrayList<TriFactor> termCosArray, ArrayList<TriFactor> cosArray) {
        for (int i = 0; i < termCosArray.size(); i++) {
            TriFactor triFactor = termCosArray.get(i);
            cosArray.add(triFactor);
            /*int flag = 0;
            for (TriFactor item : cosArray) {
                if (item.getSymbolTri().equals(triFactor.getSymbolTri()) &&
                        item.getCoefTri().compareTo(triFactor.getCoefTri()) == 0 &&
                        item.getIndexTri().compareTo(triFactor.getIndexTri()) == 0) {
                    item.setIsIndex(item.getIsIndex() + 1);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                cosArray.add(term.getCosArray().get(i));
            }*/
        }
    }

    public int compareTo(Term other) {    //按指数将单项式从高到低排序
        if (this.getIndexTerm().compareTo(other.getIndexTerm()) < 0) {
            return 1;
        } else if (this.getIndexTerm().compareTo(other.getIndexTerm()) > 0) {
            return -1;
        } else { //指数相同的按sin cos个数和从小到大排列
            if ((this.getSinArray().size() + this.getCosArray().size()) <
                    (other.getSinArray().size() + other.getCosArray().size())) {
                return -1;
            } else if ((this.getSinArray().size() + this.getCosArray().size()) >
                    (other.getSinArray().size() + other.getCosArray().size())) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}