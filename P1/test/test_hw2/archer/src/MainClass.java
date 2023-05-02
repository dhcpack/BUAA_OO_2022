import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.math.BigInteger;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        ExprInput inPut = new ExprInput(ExprInputMode.ParsedMode);
        ArrayList<Poly> polys = new ArrayList<>();   //fn对应的多项表达式
        ArrayList<String> tagExps = new ArrayList<>();  //带标签的预解析子表达式
        int expNum = inPut.getCount();
        for (int i = 0; i < expNum; i++) {
            String curExpr = inPut.readLine();
            tagExps.add(curExpr);
        }
        for (String item : tagExps) {
            String[] details = item.split(" ");
            if (details[1].equals("add")) { //add
                MainClass.addOp(polys, details);
            } else if (details[1].equals("sub")) {  //sub
                MainClass.subOp(polys, details);
            } else if (details[1].equals("mul")) { //mul
                MainClass.mulOp(polys, details);
            } else if (details[1].equals("pow")) { //pow
                MainClass.powOp(polys, details);
            } else if (details[1].equals("pos")) { //pos
                MainClass.posOp(polys, details);
            } else if (details[1].equals("neg")) { //neg
                MainClass.negOp(polys, details);
            } else if (details[1].equals("sin")) { //sin
                MainClass.sinOp(polys, details);
            } else if (details[1].equals("cos")) { //cos
                MainClass.cosOp(polys, details);
            } else {                               //null
                ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
                Term term1 = MainClass.getTerm(details[1]);  //生成一个操作数项
                tempTerms1.add(term1);         //并将操作数项加入临时list以生成多项式
                polys.add(new Poly(tempTerms1));   //直接将生成的多项式作为tag==fi结果
            }
        }
        //print term
        Poly lastPoly = polys.get(polys.size() - 1);
        Output output = new Output(lastPoly);
        Poly ansPoly = output.mergePoly(lastPoly);
        if (ansPoly.getTerms().size() == 0) {  //全为0
            System.out.print(0);
        } else {
            /*ArrayList<Term> printTerms = new ArrayList<>();
            for (Term item : ansPoly.getTerms()) {
                printTerms.add(output.triPow(item));
            }
            Poly printPoly = new Poly(printTerms);
            int flag = MainClass.printFirst(printPoly, output);
            for (int i = 0; i < printPoly.getTerms().size(); i++) {
                if (i != flag) {
                    output.print(printPoly.getTerms().get(i));
                }
            }*/
            int flag = MainClass.printFirst(ansPoly, output);
            for (int i = 0; i < ansPoly.getTerms().size(); i++) {
                if (i != flag) {
                    output.print(ansPoly.getTerms().get(i));
                }
            }
        }
    }

    public static Poly getPoly(String str, ArrayList<Term> tempTerms, ArrayList<Poly> polys) {
        Poly poly;
        if (str.charAt(0) == 'f') {       //已生成的子表达式为操作数
            int polyNum = Integer.parseInt(str.substring(1));
            poly = polys.get(polyNum - 1);
        } else {
            Term term1 = MainClass.getTerm(str);  //生成一个操作数项
            tempTerms.add(term1);         //并将操作数项加入临时list以生成多项式
            poly = new Poly(tempTerms);
        }
        return poly;
    }

    public static Term getTerm(String str) {
        String symbol = MainClass.getSymbol(str);
        BigInteger index = MainClass.getIndex(str);
        BigInteger coef = MainClass.getCoef(str);
        ArrayList<TriFactor> sinArray = new ArrayList<>();
        ArrayList<TriFactor> cosArray = new ArrayList<>();
        Term term = new Term(index, coef, symbol, sinArray, cosArray);
        return term;
    }

    public static String getSymbol(String str) {
        if (str.charAt(0) == '-') {
            return "-";
        } else {
            return "+";
        }
    }

    public static BigInteger getCoef(String str) {
        int len = str.length();
        BigInteger ans = new BigInteger("0");
        if (str.charAt(len - 1) == 'x') {  //变量项
            return new BigInteger("1");
        } else {                         //常量项 去前导0
            for (int i = 0; i < len; i++) {
                if (Character.isDigit(str.charAt(i))) {
                    ans = ans.multiply(new BigInteger("10")).add(
                            new BigInteger(String.valueOf(str.charAt(i) - '0')));
                }
            }
            return ans;
        }
    }

    public static BigInteger getIndex(String str) {
        int len = str.length();
        if (str.charAt(len - 1) == 'x') {  //变量项
            return new BigInteger("1");
        } else {
            return new BigInteger("0");          //常量指数为0
        }
    }

    public static TriFactor getTriFactor(String str, ArrayList<Poly> polys) { //可能为fx或常数或幂函数
        String symbol;
        BigInteger coef;
        BigInteger index;
        TriFactor triFactor;
        if (str.charAt(0) == 'f') {    //操作数为fx(poly) 仅有一个term 为常数或幂函数
            int polyNum = Integer.parseInt(str.substring(1));
            Poly poly = polys.get(polyNum - 1);  //得到多项式
            Term term = poly.getFirstTerm();     //得到唯一一项
            symbol = term.getSymbolTerm();
            coef = term.getCoefTerm();
            index = term.getIndexTerm();
            triFactor = new TriFactor(index, coef, symbol, new BigInteger("1"));
        } else {     //操作数为常数或幂函数
            symbol = MainClass.getSymbol(str);
            coef = MainClass.getCoef(str);
            index = MainClass.getIndex(str);
            triFactor = new TriFactor(index, coef, symbol, new BigInteger("1"));
        }
        return triFactor;
    }

    public static void addOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<Term> tempTerms2 = new ArrayList<>();
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //opNum2 = details[3];
        Poly poly2 = MainClass.getPoly(details[3], tempTerms2, polys);
        //new add
        AddPoly addPoly = new AddPoly(poly1, poly2);
        polys.add(addPoly.add(poly1, poly2));
    }

    public static void subOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<Term> tempTerms2 = new ArrayList<>();
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //opNum2 = details[3];
        Poly poly2 = MainClass.getPoly(details[3], tempTerms2, polys);
        //new add
        SubPoly subPoly = new SubPoly(poly1, poly2);
        polys.add(subPoly.sub(poly1, poly2));
    }

    public static void mulOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<Term> tempTerms2 = new ArrayList<>();
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //opNum2 = details[3];
        Poly poly2 = MainClass.getPoly(details[3], tempTerms2, polys);
        MulPoly mulPoly = new MulPoly(poly1, poly2);
        Poly poly = mulPoly.mul(poly1, poly2);
        polys.add(poly);
    }

    public static void powOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<Term> tempTerms2 = new ArrayList<>();
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //opNum2 = details[3];
        Poly poly2 = MainClass.getPoly(details[3], tempTerms2, polys);
        MulPoly mulPoly = new MulPoly(poly1, poly2);
        polys.add(mulPoly.pow(poly1, poly2));
    }

    public static void posOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //new pos
        AddPoly addPoly = new AddPoly(poly1);
        polys.add(addPoly.pos(poly1));
    }

    public static void negOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        Poly poly1 = MainClass.getPoly(details[2], tempTerms1, polys);
        //new neg
        SubPoly subPoly = new SubPoly(poly1);
        polys.add(subPoly.neg(poly1));
    }

    public static void sinOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<TriFactor> sinArray = new ArrayList<>();
        ArrayList<TriFactor> cosArray = new ArrayList<>();
        TriFactor triFactor = MainClass.getTriFactor(details[2], polys);
        sinArray.add(triFactor);
        Term term1 = new Term(new BigInteger("0"), new BigInteger("1"), "+",
                sinArray, cosArray);
        tempTerms1.add(term1);
        polys.add(new Poly(tempTerms1));
    }

    public static void cosOp(ArrayList<Poly> polys, String[] details) {
        ArrayList<Term> tempTerms1 = new ArrayList<>();   //临时terms
        ArrayList<TriFactor> sinArray = new ArrayList<>();
        ArrayList<TriFactor> cosArray = new ArrayList<>();
        TriFactor triFactor = MainClass.getTriFactor(details[2], polys);
        cosArray.add(triFactor);
        Term term1 = new Term(new BigInteger("0"), new BigInteger("1"), "+",
                sinArray, cosArray);
        tempTerms1.add(term1);
        polys.add(new Poly(tempTerms1));
    }

    public static int printFirst(Poly ansPoly, Output output) {
        int flag = -1;
        for (int i = 0; i < ansPoly.getTerms().size(); i++) {
            if (ansPoly.getTerms().get(i).getSymbolTerm().equals("+")) {
                flag = i;
                break;
            }
        }
        if (flag != -1) { //有正项
            output.printFirstTerm(ansPoly.getTerms().get(flag));   //打印首个为+的项
        }
        return flag;
    }
}