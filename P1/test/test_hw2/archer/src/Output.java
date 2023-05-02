import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Output {
    private Poly poly;

    public Output(Poly poly) {
        this.poly = poly;
    }

    public Poly mergePoly(Poly poly) { //合并同类项
        ArrayList<Term> sorted = new ArrayList<>(poly.getTerms());
        Collections.sort(sorted);  //按指数由大到小排列;指数相同按sin cos个数由小到大排列
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).getIndexTerm().compareTo(sorted.get(i + 1).getIndexTerm()) == 0) {
                //条件2 判断sin cos是否相同
                if (sorted.get(i).getSinArray().size() == sorted.get(i + 1).getSinArray().size() &&
                        sorted.get(i).getCosArray().size() ==
                                sorted.get(i + 1).getCosArray().size()) {
                    //sin cos个数分别相同
                    ArrayList<TriFactor> sortedSin1 = new ArrayList<>(sorted.get(i).getSinArray());
                    ArrayList<TriFactor> sortedCos1 = new ArrayList<>(sorted.get(i).getCosArray());
                    ArrayList<TriFactor> sortedSin2 =
                            new ArrayList<>(sorted.get(i + 1).getSinArray());
                    ArrayList<TriFactor> sortedCos2 =
                            new ArrayList<>(sorted.get(i + 1).getCosArray());
                    //将sin cos按指数由大到小排列
                    Collections.sort(sortedSin1);  //按指数由大到小排列
                    Collections.sort(sortedCos1);
                    Collections.sort(sortedSin2);  //按指数由大到小排列
                    Collections.sort(sortedCos2);
                    int flag = 0;     //先置0
                    for (int j = 0; j < sortedSin1.size(); j++) {
                        if (!sortedSin1.get(j).getSymbolTri().equals(
                                sortedSin2.get(j).getSymbolTri()) ||
                                sortedSin1.get(j).getCoefTri().compareTo(
                                        sortedSin2.get(j).getCoefTri()) != 0 ||
                                sortedSin1.get(j).getIndexTri().compareTo(
                                        sortedSin2.get(j).getIndexTri()) != 0) {
                            flag = 1;  //sin存在不相等的
                            break;
                        }
                    }
                    if (flag != 1) {  //sin相等
                        for (int k = 0; k < sortedCos1.size(); k++) {
                            if (!sortedCos1.get(k).getSymbolTri().equals(
                                    sortedCos2.get(k).getSymbolTri()) ||
                                    sortedCos1.get(k).getCoefTri().compareTo(
                                            sortedCos2.get(k).getCoefTri()) != 0 ||
                                    sortedCos1.get(k).getIndexTri().compareTo(
                                            sortedCos2.get(k).getIndexTri()) != 0) {
                                flag = 1;
                                break;
                            }
                        }
                    }
                    if (flag == 0) {
                        //调用合并算法;
                        Output.merge(sorted, i);
                    }
                }
            }
        }
        Poly mergePoly = Output.returnPoly(sorted);
        return mergePoly;
    }

    public static void merge(ArrayList<Term> sorted, int i) {
        BigInteger coef1;
        BigInteger coef2;
        if (sorted.get(i + 1).getSymbolTerm().equals("+")) {
            coef1 = sorted.get(i + 1).getCoefTerm();
        } else {
            coef1 = sorted.get(i + 1).getCoefTerm().multiply(new BigInteger("-1"));
        }
        if (sorted.get(i).getSymbolTerm().equals("+")) {
            coef2 = sorted.get(i).getCoefTerm();
        } else {
            coef2 = sorted.get(i).getCoefTerm().multiply(new BigInteger("-1"));
        }
        sorted.get(i + 1).setCoefTerm(coef1.add(coef2));
        if (sorted.get(i + 1).getCoefTerm().compareTo(new BigInteger("0")) > 0) {
            //符号变为+ coef保持+
            sorted.get(i + 1).setSymbolTerm("+");
        } else {
            //符号变为-
            sorted.get(i + 1).setSymbolTerm("-");
            //coef再变为+
            sorted.get(i + 1).setCoefTerm(sorted.get(
                    i + 1).getCoefTerm().multiply(new BigInteger("-1")));
        }
        sorted.get(i).setCoefTerm(new BigInteger("0"));  //将前一个系数置0
    }

    public static Poly returnPoly(ArrayList<Term> sorted) {
        ArrayList<Term> mergeSorted = new ArrayList<>();
        for (Term item : sorted) {
            int tag = 0;
            if (item.getCoefTerm().compareTo(new BigInteger("0")) == 0) {
                tag = 1;
            } else {
                for (TriFactor triFactor : item.getSinArray()) {
                    if (triFactor.getCoefTri().compareTo(new BigInteger("0")) == 0) {
                        tag = 1;
                        break;
                    }
                }
                for (TriFactor triFactor : item.getCosArray()) {
                    if (triFactor.getCoefTri().compareTo(new BigInteger("90")) == 0) {
                        tag = 1;
                        break;
                    }
                }
            }
            if (tag == 0) {
                mergeSorted.add(item);
            }
        }
        Poly mergePoly = new Poly(mergeSorted);
        return mergePoly;
    }

    public void printFirstTerm(Term term) {
        int flag = 0;
        if (term.getIndexTerm().compareTo(new BigInteger("0")) == 0) { //常数项
            if ((term.getSinArray().size() != 0 ||
                    term.getCosArray().size() != 0) &&
                    term.getCoefTerm().compareTo(new BigInteger("1")) == 0) {
                flag = 1;
            } else {
                System.out.print(term.getCoefTerm());
            }
        } else { //幂函数项
            if (term.getCoefTerm().compareTo(new BigInteger("1")) == 0) {  //系数1
                if (term.getIndexTerm().compareTo(new BigInteger("1")) == 0) {
                    //index=1
                    System.out.print("x");
                } else {
                    System.out.print("x**" + term.getIndexTerm());
                }
            } else { //系数不为1
                if (term.getIndexTerm().compareTo(new BigInteger("1")) == 0) {
                    //index=1
                    System.out.print(term.getCoefTerm() + "*x");
                } else {
                    System.out.print(term.getCoefTerm() + "*x**" + term.getIndexTerm());
                }
            }
        }
        //打印三角函数项
        if (term.getSinArray().size() != 0 || term.getCosArray().size() != 0) {
            Output.printSin(term, flag);  //print sin
            Output.printCos(term, flag);  //print cos
        }
    }

    public void print(Term term) {    //将多项式中的一项一项打印
        int flag = 0;
        if (term.getIndexTerm().compareTo(new BigInteger("0")) == 0) {      //常数项
            if ((term.getSinArray().size() != 0 ||
                    term.getCosArray().size() != 0) &&
                    term.getCoefTerm().compareTo(new BigInteger("1")) == 0) {
                System.out.print(term.getSymbolTerm());
                flag = 1;
            } else {
                System.out.print(term.getSymbolTerm() + term.getCoefTerm());
            }
        } else {             //幂函数项
            if (term.getCoefTerm().compareTo(new BigInteger("1")) == 0) {    //term.getCoef() == 1
                if (term.getIndexTerm().compareTo(new BigInteger("1")) == 0) {
                    //index=1
                    System.out.print(term.getSymbolTerm() + "x");
                } else {
                    System.out.print(term.getSymbolTerm() + "x**" + term.getIndexTerm());
                }
            } else {
                if (term.getIndexTerm().compareTo(new BigInteger("1")) == 0) {
                    //index=1
                    System.out.print(term.getSymbolTerm() + term.getCoefTerm() + "*x");
                } else {
                    System.out.print(term.getSymbolTerm() + term.getCoefTerm() + "*x**" +
                            term.getIndexTerm());
                }
            }
        }
        //打印三角函数项
        if (term.getSinArray().size() != 0 || term.getCosArray().size() != 0) {
            Output.printSin(term, flag);  //print sin
            Output.printCos(term, flag);  //print cos
        }
    }

    public static void printSin(Term term, int flag) {
        if (flag != 1 && term.getSinArray().size() != 0) {
            System.out.print("*");
        }
        int i = 0;
        for (TriFactor item : term.getSinArray()) {
            if (i != 0 && item.getCoefTri().compareTo(new BigInteger("90")) != 0) {
                System.out.print("*");
            }

            if (item.getCoefTri().compareTo(new BigInteger("90")) == 0) {
                if (item.getSymbolTri().equals("+")) {
                    System.out.print("1");
                } else {
                    System.out.print("-1");
                }
            } else {
                if (item.getIndexTri().compareTo(new BigInteger("0")) == 0) { //常数
                    if (item.getIsIndex().compareTo(new BigInteger("1")) == 0) {
                        if (item.getSymbolTri().equals("+")) {
                            System.out.print("sin(" + item.getCoefTri() + ")");
                        } else {
                            System.out.print("sin(" + item.getSymbolTri() +
                                    item.getCoefTri() + ")");
                        }
                    } else {
                        if (item.getSymbolTri().equals("+")) {
                            System.out.print("sin(" + item.getCoefTri() + ")**" +
                                    item.getIsIndex());
                        } else {
                            System.out.print("sin(" + item.getSymbolTri() +
                                    item.getCoefTri() + ")**" + item.getIsIndex());
                        }
                    }
                } else { //幂函数
                    if (item.getIsIndex().compareTo(new BigInteger("1")) == 0) {
                        if (item.getIndexTri().compareTo(new BigInteger("1")) == 0) {
                            //系数1
                            System.out.print("sin(" + "x" + ")");
                        } else {
                            System.out.print("sin(" + "x**" + item.getIndexTri() + ")");
                        }
                    } else {
                        if (item.getIndexTri().compareTo(new BigInteger("1")) == 0) {
                            //系数1
                            System.out.print("sin(" + "x" + ")**" + item.getIsIndex());
                        } else {
                            System.out.print("sin(" + "x**" + item.getIndexTri() +
                                    ")**" + item.getIsIndex());
                        }
                    }
                }
            }
            i++;
        }
    }

    public static void printCos(Term term, int flag) {
        if (term.getSinArray().size() != 0) { //前有sin
            if (term.getCosArray().size() != 0) {
                System.out.print("*");
            }
        } else {  //前无sin
            if (flag != 1 && term.getCosArray().size() != 0) {
                System.out.print("*");
            }
        }
        int i = 0;
        for (TriFactor item : term.getCosArray()) {
            if (i != 0 && item.getCoefTri().compareTo(new BigInteger("0")) != 0) {
                System.out.print("*");
            }
            if (item.getCoefTri().compareTo(new BigInteger("0")) == 0) { //系数为0 sin0
                if (item.getSymbolTri().equals("+")) {
                    System.out.print("1");
                } else {
                    System.out.print("-1");
                }
            } else {
                if (item.getIndexTri().compareTo(new BigInteger("0")) == 0) { //常数
                    if (item.getIsIndex().compareTo(new BigInteger("1")) == 0) {
                        if (item.getSymbolTri().equals("+")) {
                            System.out.print("cos(" + item.getCoefTri() + ")");
                        } else {
                            System.out.print("cos(" + item.getSymbolTri() +
                                    item.getCoefTri() + ")");
                        }
                    } else {
                        if (item.getSymbolTri().equals("+")) {
                            System.out.print("cos(" + item.getCoefTri() + ")**" +
                                    item.getIsIndex());
                        } else {
                            System.out.print("cos(" + item.getSymbolTri() +
                                    item.getCoefTri() + ")**" + item.getIsIndex());
                        }
                    }
                } else { //幂函数
                    if (item.getIsIndex().compareTo(new BigInteger("1")) == 0) {
                        if (item.getIndexTri().compareTo(new BigInteger("1")) == 0) {
                            //系数1
                            System.out.print("cos(" + "x" + ")");
                        } else {
                            System.out.print("cos(" + "x**" + item.getIndexTri() + ")");
                        }
                    } else {
                        if (item.getIndexTri().compareTo(new BigInteger("1")) == 0) {
                            //系数1
                            System.out.print("cos(" + "x" + ")**" + item.getIsIndex());
                        } else {
                            System.out.print("cos(" + "x**" + item.getIndexTri() +
                                    ")**" + item.getIsIndex());
                        }
                    }
                }
            }
            i++;
        }
    }
}