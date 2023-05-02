package poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Polynomial
{
    private ArrayList<PolyTerm> polyTerms;

    /**
     * 采用这种构造方式，其实就是构造了一个 0 的 Polynomial
     */
    public Polynomial()
    {
        polyTerms = new ArrayList<>();
    }

    /**
     * 采用这种构造方式，构造了首项是 polyTerm 的多项式
     */
    public Polynomial(PolyTerm polyTerm)
    {
        this.polyTerms = new ArrayList<>();
        this.polyTerms.add(polyTerm);
    }

    public ArrayList<PolyTerm> getPolyTerms()
    {
        return polyTerms;
    }

    public void addTerms(Polynomial polynomial)
    {
        polyTerms.addAll(polynomial.getPolyTerms());
    }

    /**
     * 最朴素的双循环算法，表达的是两个多项式相乘，而最后会落实到项与项的相乘
     * @param other
     * @return 需要注意的是，返回值才是计算后的结果，这与 BigInteger类的计算类似
     */
    public Polynomial multiply(Polynomial other)
    {
        Polynomial p = new Polynomial();

        for (PolyTerm polyTerm1 : this.polyTerms)
        {
            for (PolyTerm polyTerm2 : other.polyTerms)
            {
                p.polyTerms.add(polyTerm1.multiply(polyTerm2));
            }
        }
        return p;
    }

    /**
     * 将项合并
     */
    public void merge()
    {
        // 去掉系数为 0 的项
        polyTerms.removeIf(e -> e.getCoefficient().equals(BigInteger.ZERO));
        for (int i = 0; i < polyTerms.size(); i++)
        {
            mergeByIndex(i);
        }
    }

    /**
     * 会使在 polyTerms 链表中 index 对应的项之后与该项系数相同的项都被合并
     * @param index
     */
    private void mergeByIndex(int index)
    {
        PolyTerm polyTerm = polyTerms.get(index);
        for (int i = index + 1; i < polyTerms.size(); i++)
        {
            if (polyTerms.get(i).getExponent().equals(polyTerm.getExponent()))
            {
                polyTerm.setCoefficient(polyTerm.getCoefficient().add(
                        polyTerms.get(i).getCoefficient()));
                polyTerms.remove(i);
                i--;
            }
        }
    }

    /**
     * 挑出一个正项放到最前面，是因为这样可以缩短字符串的长度
     */
    private void movePositiveToFront()
    {
        for (PolyTerm polyTerm : polyTerms)
        {
            if (polyTerm.getCoefficient().compareTo(BigInteger.ZERO) > 0)
            {
                polyTerms.remove(polyTerm);
                polyTerms.add(0, polyTerm);
                break;
            }
        }
    }

    @Override
    public String toString()
    {
        movePositiveToFront();

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < polyTerms.size(); i++)
        {
            s.append(polyTerms.get(i).toString());
            if (i < polyTerms.size() - 1)
            {
                //只有正号需要打印
                if (polyTerms.get(i + 1).getCoefficient().compareTo(BigInteger.ZERO) > 0)
                {
                    s.append("+");
                }
            }
        }
        if (s.toString().equals(""))
        {
            s = new StringBuilder("0");
        }
        return s.toString();
    }
}
