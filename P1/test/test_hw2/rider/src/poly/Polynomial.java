package poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

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

    public Polynomial clone()
    {
        Polynomial copy = new Polynomial();
        for (PolyTerm polyTerm : polyTerms)
        {
            copy.addPolyTerm(polyTerm.clone());
        }

        return copy;
    }

    public void addPolyTerm(PolyTerm polyTerm)
    {
        polyTerms.add(polyTerm);
    }

    public Polynomial add(Polynomial other)
    {
        Polynomial polynomial = clone();
        for (PolyTerm polyTerm : other.polyTerms)
        {
            polynomial.addPolyTerm(polyTerm.clone());
        }

        return polynomial;
    }

    public Polynomial multi(Polynomial other)
    {
        Polynomial polynomial = new Polynomial();

        for (PolyTerm polyTerm1 : this.polyTerms)
        {
            for (PolyTerm polyTerm2 : other.polyTerms)
            {
                polynomial.addPolyTerm(polyTerm1.multi(polyTerm2));
            }
        }
        return polynomial;
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
            if (polyTerms.get(i).isSameType(polyTerm))
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
        merge();
        movePositiveToFront();
        String s = "";
        for (int i = 0; i < polyTerms.size(); i++)
        {
            s += polyTerms.get(i).toString();
            if (i < polyTerms.size() - 1)
            {
                if (polyTerms.get(i + 1).getCoefficient()
                        .compareTo(BigInteger.ZERO) > 0)
                {
                    s += "+";
                }
            }
        }
        if (s.equals(""))
        {
            s = "0";
        }
        return s;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Polynomial that = (Polynomial) o;
        return Objects.equals(polyTerms, that.polyTerms);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(polyTerms);
    }
}
